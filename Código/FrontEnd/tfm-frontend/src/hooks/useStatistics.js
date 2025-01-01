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
      try {
        setLoading(true);
        const token = localStorage.getItem("token");
        const physiotherapistId = localStorage.getItem("id");
        console.log(physiotherapistId);

        if (!physiotherapistId) {
          setError("No se encontró el ID del fisioterapeuta.");
          return;
        }

        const [generalResponse, completedResponse, ratesResponse] = await Promise.all([
          questionnaireService.get(`/questionnaires/statistics?physiotherapistId=${physiotherapistId}`, {
            headers: { Authorization: `Bearer ${token}` },
          }),
          questionnaireService.get(`/questionnaires/completed-by-patient?physiotherapistId=${physiotherapistId}`, {
            headers: { Authorization: `Bearer ${token}` },
          }),
          questionnaireService.get(`/questionnaires/completion-rates?physiotherapistId=${physiotherapistId}`, {
            headers: { Authorization: `Bearer ${token}` },
          }),
        ]);

        setGeneralStats(generalResponse.data);
        setCompletedByPatient(completedResponse.data);
        setCompletionRates(ratesResponse.data);
      } catch (err) {
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
