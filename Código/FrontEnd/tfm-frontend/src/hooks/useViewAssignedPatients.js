import { useState, useEffect } from "react";
import { physiotherapistService, questionnaireService } from "../api/apiConfig";

const useViewAssignedPatients = () => {
  const [patients, setPatients] = useState([]);
  const [questionnaires, setQuestionnaires] = useState([]);
  const [selectedPatient, setSelectedPatient] = useState("");
  const [selectedQuestionnaire, setSelectedQuestionnaire] = useState(null);
  const [responses, setResponses] = useState([]);
  const [comments, setComments] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState("");

  useEffect(() => {
    const fetchPatients = async () => {
      try {
        setLoading(true);
        const token = localStorage.getItem("token");
        const physiotherapistId = localStorage.getItem("id");

        const response = await physiotherapistService.get(
          `/assignments/physiotherapist/${physiotherapistId}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        setPatients(response.data);
      } catch (err) {
        setError("Error al cargar los pacientes.");
      } finally {
        setLoading(false);
      }
    };

    fetchPatients();
  }, []);

  useEffect(() => {
    const fetchQuestionnaires = async () => {
      if (!selectedPatient) {
        setQuestionnaires([]);
        return;
      }

      try {
        setLoading(true);
        const token = localStorage.getItem("token");

        const response = await questionnaireService.get(
          `/questionnaires/${selectedPatient}/in-progress`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        if (response.status === 200 && response.data.length === 0) {
          setError("No hay cuestionarios en progreso para este paciente.");
          setQuestionnaires([]);
        } else {
          setQuestionnaires(response.data);
        }
      } catch (err) {
        if (err.response && err.response.status === 404) {
          setError("No se encontraron cuestionarios en progreso.");
        } else {
          setError("Error al cargar los cuestionarios.");
        }
      } finally {
        setLoading(false);
      }
    };

    fetchQuestionnaires();
  }, [selectedPatient]);

  const fetchResponses = async (questionnaireId) => {
    try {
      setLoading(true);
      const token = localStorage.getItem("token");

      const response = await questionnaireService.get(
        `/questionnaires/${selectedPatient}/${questionnaireId}/detailed-responses`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      setResponses(response.data);
    } catch (err) {
      setError("Error al cargar las respuestas del cuestionario.");
    } finally {
      setLoading(false);
    }
  };

  const handleSaveComments = async () => {
    try {
      setLoading(true);
      const token = localStorage.getItem("token");

      await questionnaireService.post(
        `/questionnaires/${selectedQuestionnaire}/add-comments`,
        comments,
        {
          params: { patientId: selectedPatient },
          headers: { Authorization: `Bearer ${token}`, "Content-Type": "text/plain" },
        }
      );

      setSuccess("Comentarios guardados correctamente.");
      setComments("");

      // Eliminar el cuestionario comentado de la lista
      setQuestionnaires((prev) =>
        prev.filter((q) => q.questionnaireId !== selectedQuestionnaire)
      );

      setSelectedQuestionnaire(null);
    } catch (err) {
      setError("Error al guardar los comentarios.");
    } finally {
      setLoading(false);
    }
  };

  return {
    patients,
    questionnaires,
    selectedPatient,
    setSelectedPatient,
    selectedQuestionnaire,
    setSelectedQuestionnaire,
    responses,
    comments,
    setComments,
    handleSaveComments,
    loading,
    error,
    success,
    fetchResponses,
  };
};

export default useViewAssignedPatients;
