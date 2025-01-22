import { useState, useEffect } from "react";
import { patientService } from "../api/apiConfig";

const usePatientProgress = (patientId, questionnaireId) => {
  const [progress, setProgress] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProgress = async () => {
      try {
        setLoading(true);
        setError(null);
        const token = localStorage.getItem("token");
        const response = await patientService.get(
          `/patients/${patientId}/questionnaires/${questionnaireId}/progress`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        setProgress(response.data);
        setLoading(false);
      } catch (err) {
        console.error("Error al cargar el progreso del paciente:", err);
        setError("No se pudo cargar el progreso del paciente. Intente nuevamente.");
        setLoading(false);
      }
    };

    if (patientId && questionnaireId) {
      fetchProgress();
    }
  }, [patientId, questionnaireId]);

  return { progress, loading, error };
};

export default usePatientProgress;
