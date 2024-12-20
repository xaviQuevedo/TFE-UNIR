import { useState } from "react";
import { patientService } from "../api/apiConfig";
import { questionnaireService } from "../api/apiConfig";

const useAnswerQuestionnaire = () => {
  const [questions, setQuestions] = useState([]);
  const [responses, setResponses] = useState({});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState("");
  const [title, setTitle] = useState("");

  const setSelectedQuestionnaire = async (questionnaire) => {
    try {
      setLoading(true);
      const token = localStorage.getItem("token");
      const response = await questionnaireService.get(
        `/questionnaires/${questionnaire.id}/questions`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      setQuestions(response.data.questions); // Asume que las opciones ya están formateadas correctamente
      setTitle(response.data.title);
      setLoading(false);
    } catch (err) {
      setError("Error al cargar el cuestionario.");
      setLoading(false);
    }
  };

  const handleResponseChange = (questionId, response) => {
    setResponses((prev) => ({ ...prev, [questionId]: response }));
  };

  const submitResponses = async () => {
    try {
      setLoading(true);
      const token = localStorage.getItem("token");
      const patientId = localStorage.getItem("id");
      await patientService.post(
        `/patients/${patientId}/submit-responses`,
        {
          responses,
        },
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      setSuccess("Respuestas enviadas correctamente.");
      setLoading(false);
    } catch (err) {
      setError("Error al enviar las respuestas.");
      setLoading(false);
    }
  };

  return {
    questions,
    responses,
    title,
    handleResponseChange,
    submitResponses,
    setSelectedQuestionnaire,
    loading,
    error,
    success,
  };
};

export default useAnswerQuestionnaire;
