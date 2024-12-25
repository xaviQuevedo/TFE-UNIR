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
  const [questionnaireId, setQuestionnaireId] = useState(null); // Ahora usamos setQuestionnaireId para actualizar el estado

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
      
      // Actualizar el ID del cuestionario
      setQuestionnaireId(questionnaire.id);

      // Guardar preguntas y título del cuestionario
      setQuestions(response.data.questions);
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
      console.log("submitResponses - Using questionnaireId:", questionnaireId);

      

      setLoading(true);
      const token = localStorage.getItem("token");
      const patientId = localStorage.getItem("id");

      // Formatear las respuestas
      const formattedResponses = Object.entries(responses).map(([questionId, answer]) => ({
        questionId: parseInt(questionId, 10),
        answer: answer.toString(),
      }));

      const payload = { responses: formattedResponses };

  
    
    console.log("submitResponses - Payload:", payload);

      // Realizar la solicitud
      await patientService.post(
        `/patients/${patientId}/questionnaires/${questionnaireId}/submit-responses`,
        payload,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      console.log("Token utilizadooooo:", token);

      console.log("submitResponses - Payload:", {
        responses: formattedResponses,
    });
    

      setSuccess("Respuestas enviadas correctamente.");
      setLoading(false);
    } catch (err) {
      console.error("Error en submitResponses:", err);
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
