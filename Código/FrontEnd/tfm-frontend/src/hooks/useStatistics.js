import { useEffect, useState } from "react";
import { questionnaireService } from "../api/apiConfig";

const useStatistics = () => {
  const [generalStats, setGeneralStats] = useState({});
  const [completedByPatient, setCompletedByPatient] = useState([]);
  const [completionRates, setCompletionRates] = useState({});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchStats = async () => {
      setLoading(true);
      setError(null);
  
      const token = localStorage.getItem("token");
      console.log("Token enviado en la solicitud:", token);

      const physiotherapistId = Number(localStorage.getItem("id"));

  
      if (!physiotherapistId) {
        console.error("No se encontró el ID del fisioterapeuta.");
        setError("No se encontró el ID del fisioterapeuta.");
        setLoading(false);
        return;
      }
  
      try {
        console.log("Fetching statistics for physiotherapist:", physiotherapistId);
  
        const [
          generalResponse,
          completedResponse,
          ratesResponse
        ] = await Promise.allSettled([
          questionnaireService.get(`/questionnaires/statistics?physiotherapistId=${physiotherapistId}`, {
            headers: { Authorization: `Bearer ${token}` },
          }),
          questionnaireService.get(`/questionnaires/completed-questionnaires?physiotherapistId=${physiotherapistId}`, {
            headers: { Authorization: `Bearer ${token}` },
          }),
          questionnaireService.get(`/questionnaires/completion-rates?physiotherapistId=${physiotherapistId}`, {
            headers: { Authorization: `Bearer ${token}` },
          }),
        ]);
  
        console.log("General Stats Response:", generalResponse);
        console.log("Completed Questionnaires Response:", completedResponse);
        console.log("Completion Rates Response:", ratesResponse);
  
        if (generalResponse.status === "fulfilled") {
          setGeneralStats(generalResponse.value.data);
        } else {
          console.error("Error en /questionnaires/statistics:", generalResponse.reason);
        }
  
        if (completedResponse.status === "fulfilled") {
          setCompletedByPatient(completedResponse.value.data);
        } else {
          console.error("Error en /questionnaires/completed-questionnaires:", completedResponse.reason);
        }
  
        if (ratesResponse.status === "fulfilled") {
          setCompletionRates(ratesResponse.value.data);
        } else {
          console.error("Error en /questionnaires/completion-rates:", ratesResponse.reason);
        }
  
        if (
          generalResponse.status === "rejected" ||
          completedResponse.status === "rejected" ||
          ratesResponse.status === "rejected"
        ) {
          setError("Error al cargar una o más estadísticas.");
        }
  
      } catch (err) {
        console.error("Error inesperado al cargar las estadísticas:", err);
        setError("Error al cargar las estadísticas.");
      } finally {
        setLoading(false);
      }
    };
  
    fetchStats();
  }, []);
  

  return {
    generalStats,
    completedByPatient,
    completionRates,
    loading,
    error,
  };
};

export default useStatistics;
