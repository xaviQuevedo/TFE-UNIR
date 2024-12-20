import { useState, useEffect } from "react";
import { patientService } from "../api/apiConfig";

const useCompleteQuestionnaires = () => {
  const [pendingQuestionnaires, setPendingQuestionnaires] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState("");
  const [currentQuestionnaire, setCurrentQuestionnaire] = useState(null);

  useEffect(() => {
    const fetchPendingQuestionnaires = async () => {
      try {
        setLoading(true);
        const token = localStorage.getItem("token");
        const patientId = localStorage.getItem("id");
        const response = await patientService.get(`/patients/${patientId}/pending-questionnaires`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setPendingQuestionnaires(response.data);
        setLoading(false);
      } catch (err) {
        setError("Error al cargar cuestionarios pendientes.");
        setLoading(false);
      }
    };

    fetchPendingQuestionnaires();
  }, []);

  return {
    pendingQuestionnaires,
    setPendingQuestionnaires,
    currentQuestionnaire,
    setCurrentQuestionnaire,
    loading,
    error,
    success,
  };
};

export default useCompleteQuestionnaires;
