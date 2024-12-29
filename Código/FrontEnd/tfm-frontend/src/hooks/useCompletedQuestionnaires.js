import { useState, useEffect } from "react";
import { patientService } from "../api/apiConfig";

const useCompletedQuestionnaires = () => {
  const [completedQuestionnaires, setCompletedQuestionnaires] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchCompletedQuestionnaires = async () => {
      try {
        setLoading(true);
        const token = localStorage.getItem("token");
        const patientId = localStorage.getItem("id");

        const response = await patientService.get(
          `/patients/${patientId}/completed-questionnaires`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        // Eliminar duplicados por ID
        const uniqueQuestionnaires = response.data.filter(
          (value, index, self) =>
            index === self.findIndex((q) => q.id === value.id)
        );

        setCompletedQuestionnaires(uniqueQuestionnaires);
      } catch (err) {
        console.error("Error al cargar cuestionarios completados:", err);
        setError("No se pudieron cargar los cuestionarios completados.");
      } finally {
        setLoading(false);
      }
    };

    fetchCompletedQuestionnaires();
  }, []);

  return { completedQuestionnaires, loading, error };
};

export default useCompletedQuestionnaires;
