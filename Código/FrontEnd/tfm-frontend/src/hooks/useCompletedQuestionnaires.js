import { useState, useEffect } from "react";
import { patientService } from "../api/apiConfig";

const useCompletedQuestionnaires = (patientId) => {
  const [completedQuestionnaires, setCompletedQuestionnaires] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchCompletedQuestionnaires = async () => {
      try {
        setLoading(true);
        const token = localStorage.getItem("token");
        const response = await patientService.getCompletedQuestionnaires(patientId, token);
        setCompletedQuestionnaires(response.data);
      } catch (err) {
        console.error("Error al obtener los cuestionarios completados:", err);
        setError("No se pudo cargar la lista de cuestionarios completados.");
      } finally {
        setLoading(false);
      }
    };

    if (patientId) {
      fetchCompletedQuestionnaires();
    }
  }, [patientId]);

  return { completedQuestionnaires, loading, error };
};

export default useCompletedQuestionnaires;
