import { useState } from "react";
import { patientService } from "../api/apiConfig";
import { questionnaireService } from "../api/apiConfig";
import { useNavigate } from "react-router-dom";

const useAnswerQuestionnaire = () => {
  const [questions, setQuestions] = useState([]);
  const [responses, setResponses] = useState({});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState("");
  const [title, setTitle] = useState("");
  const [questionnaireId, setQuestionnaireId] = useState(null);
  const navigate = useNavigate();

  const setSelectedQuestionnaire = async (questionnaire) => {
    try {
      setLoading(true);
      setError(null);
      const token = localStorage.getItem("token");
      const response = await questionnaireService.get(
        `/questionnaires/${questionnaire.id}/questions`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      // Actualizar el ID del cuestionario y cargar preguntas
      setQuestionnaireId(questionnaire.id);
      setQuestions(response.data.questions);
      setTitle(response.data.title);
      setLoading(false);
    } catch (err) {
      console.error("Error al cargar el cuestionario:", err);
      setError("No se pudo cargar el cuestionario. Intente nuevamente.");
      setLoading(false);
    }
  };

  const handleResponseChange = (questionId, response) => {
    setResponses((prev) => ({ ...prev, [questionId]: response }));
  };

  const submitResponses = async () => {
    try {
      setLoading(true);
      setError(null);

      const token = localStorage.getItem("token");
      const patientId = localStorage.getItem("id");

      // Formatear las respuestas
      const formattedResponses = Object.entries(responses).map(
        ([questionId, answer]) => ({
          questionId: parseInt(questionId, 10),
          answer: answer.toString(),
        })
      );

      const payload = { responses: formattedResponses };

      console.log("Enviando respuestas con payload:", payload);

      // Realizar la solicitud al endpoint correcto del backend
      const response = await patientService.post(
        `/patients/${questionnaireId}/submit-responses?patientId=${patientId}`,
        payload,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      console.log("Respuesta del backend:", response);
      setSuccess("Respuestas enviadas correctamente.");
      alert(
        "Respuestas enviadas correctamente. Será redirigido a los cuestionarios pendientes."
      );
      navigate("/patient/pending-questionnaires"); // Redirige después de enviar las respuestas
    } catch (err) {
      console.error("Error en submitResponses:", err);
      if (err.response?.data) {
        setError(err.response.data); // Mensaje del backend
      } else {
        setError("No se pudieron enviar las respuestas. Intente nuevamente.");
      }
    } finally {
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
