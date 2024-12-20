import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import useAnswerQuestionnaire from "../../hooks/useAnswerQuestionnaire";
import "../../styles/AnswerQuestionnaire.css";

const AnswerQuestionnaire = () => {
  const {
    questions,
    responses,
    handleResponseChange,
    submitResponses,
    loading,
    error,
    success,
    setSelectedQuestionnaire,
  } = useAnswerQuestionnaire();
  const { questionnaireId } = useParams();
  const navigate = useNavigate();
  const [loadedQuestionnaireId, setLoadedQuestionnaireId] = useState(null);

  useEffect(() => {
    // Evita múltiples llamadas para el mismo cuestionario
    if (questionnaireId !== loadedQuestionnaireId) {
      setSelectedQuestionnaire({ id: questionnaireId });
      setLoadedQuestionnaireId(questionnaireId);
    }
  }, [questionnaireId, loadedQuestionnaireId, setSelectedQuestionnaire]);

  const handleBack = () => {
    navigate("/patient/pending-questionnaires");
  };

  const renderOptions = (options) => {
    if (typeof options === "object" && options !== null) {
      return Object.entries(options).map(([key, value]) => (
        <option key={key} value={key}>
          {value}
        </option>
      ));
    }
    return null;
  };

  return (
    <div className="answer-questionnaire-container">
      <h1>Responder Cuestionario</h1>
      {error && <p className="error-message">{error}</p>}
      {success && (
        <p className="success-message">
          {success}{" "}
          <button onClick={handleBack}>
            Volver a Cuestionarios Pendientes
          </button>
        </p>
      )}
      {loading ? (
        <p>Cargando...</p>
      ) : questions && questions.length > 0 ? (
        <div>
          {questions.map((question) => (
            <div key={question.id} className="question-item">
              <p>{question.text}</p>
              {question.type === "text" && (
                <input
                  type="text"
                  value={responses[question.id] || ""}
                  onChange={(e) =>
                    handleResponseChange(question.id, e.target.value)
                  }
                />
              )}
              {question.type === "scale" && question.options ? (
                <select
                  value={responses[question.id] || ""}
                  onChange={(e) =>
                    handleResponseChange(question.id, e.target.value)
                  }
                >
                  <option value="">Seleccionar...</option>
                  {renderOptions(question.options)}
                </select>
              ) : question.type === "scale" ? (
                <input
                  type="number"
                  min="0"
                  max="10"
                  value={responses[question.id] || ""}
                  onChange={(e) =>
                    handleResponseChange(question.id, e.target.value)
                  }
                />
              ) : null}
              {question.type === "multiple" && (
                <select
                  value={responses[question.id] || ""}
                  onChange={(e) =>
                    handleResponseChange(question.id, e.target.value)
                  }
                >
                  <option value="">Seleccionar...</option>
                  {renderOptions(question.options)}
                </select>
              )}
            </div>
          ))}

          <button onClick={submitResponses} disabled={loading}>
            {loading ? "Enviando..." : "Enviar Respuestas"}
          </button>
        </div>
      ) : (
        <p>No hay preguntas disponibles para este cuestionario.</p>
      )}
    </div>
  );
};

export default AnswerQuestionnaire;
