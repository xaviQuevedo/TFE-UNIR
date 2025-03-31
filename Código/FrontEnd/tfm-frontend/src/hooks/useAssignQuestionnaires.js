import { useState, useEffect } from "react";
import { physiotherapistService } from "../api/apiConfig";
import { questionnaireService } from "../api/apiConfig";

const useAssignQuestionnaires = () => {
  const [patients, setPatients] = useState([]);
  const [questionnaires, setQuestionnaires] = useState([]);
  const [selectedPatient, setSelectedPatient] = useState("");
  const [selectedQuestionnaires, setSelectedQuestionnaires] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState("");

  // Cargar los pacientes al iniciar
  useEffect(() => {
    const fetchPatients = async () => {
      try {
        setLoading(true);
        const token = localStorage.getItem("token");
        const physiotherapistId = localStorage.getItem("id");

        if (!physiotherapistId) {
          throw new Error("ID del fisioterapeuta no encontrado.");
        }

        const response = await physiotherapistService.get(
          `/assignments/physiotherapists/${physiotherapistId}/patients`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        console.log(response.data);

        setPatients(response.data);
        setLoading(false);
      } catch (err) {
        setError(
          err.response?.data?.message || "Error al cargar los pacientes."
        );
        setLoading(false);
      }
    };

    fetchPatients();
  }, []);

  // Actualizar cuestionarios disponibles cuando cambie el paciente seleccionado
useEffect(() => {
  const fetchAvailableQuestionnaires = async () => {
    if (!selectedPatient) {
      setQuestionnaires([]);
      return;
    }

    try {
      setLoading(true);
      const token = localStorage.getItem("token");

      const response = await questionnaireService.get(
        `/questionnaires/patients/${selectedPatient}/unassigned`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      setQuestionnaires(response.data);
      setLoading(false);
    } catch (err) {
      console.error("Error al cargar cuestionarios:", err);
      setError("Error al cargar los cuestionarios disponibles.");
      setLoading(false);
    }
  };

  fetchAvailableQuestionnaires();
}, [selectedPatient]);


  const handleAssign = async () => {
    if (!selectedPatient || selectedQuestionnaires.length === 0) {
      setError("Selecciona un paciente y al menos un cuestionario.");
      return;
    }

    try {
      setLoading(true);
      const token = localStorage.getItem("token");

      await questionnaireService.post(
        "/questionnaires/assignments",
        {
          patientId: selectedPatient,
          physiotherapistId: localStorage.getItem("id"),
          questionnaireIds: selectedQuestionnaires,
        },
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      setSuccess("Cuestionarios asignados correctamente.");
      setSelectedPatient("");
      setSelectedQuestionnaires([]);
      setLoading(false);
    } catch (err) {
      setError(
        err.response?.data?.message || "Error al asignar cuestionarios."
      );
      setLoading(false);
    }
  };

  return {
    patients,
    questionnaires,
    selectedPatient,
    setSelectedPatient,
    selectedQuestionnaires,
    setSelectedQuestionnaires,
    handleAssign,
    error,
    loading,
    success,
  };
};

export default useAssignQuestionnaires;
