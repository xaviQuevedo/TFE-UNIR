import { useState, useEffect } from "react";
import { questionnaireService } from "../api/apiConfig";

const useQuestionnaireResponses = (patientId, questionnaireId) => {
  const [responses, setResponses] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchResponses = async () => {
      try {
        setLoading(true);
        setError(null);
        const token = localStorage.getItem("token");

        console.log(
          "Fetching responses for:",
          `Patient ID: ${patientId}, Questionnaire ID: ${questionnaireId}`
        );

        const response = await questionnaireService.get(
          `questionnaires/${patientId}/questionnaires/${questionnaireId}/scores`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        console.log("Backend Response Data:", response.data); // Log de la respuesta del backend
        setResponses(response.data);
        setLoading(false);
      } catch (err) {
        console.error("Error al cargar las respuestas del cuestionario:", err);
        setError("No se pudo cargar las respuestas del cuestionario.");
        setLoading(false);
      }
    };

    if (patientId && questionnaireId) {
      fetchResponses();
    }
  }, [patientId, questionnaireId]);

  return { responses, loading, error };
};

export default useQuestionnaireResponses;
