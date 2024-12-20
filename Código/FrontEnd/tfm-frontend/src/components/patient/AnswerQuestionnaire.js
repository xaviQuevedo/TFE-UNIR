import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import useAnswerQuestionnaire from "../../hooks/useAnswerQuestionnaire";

import "../../styles/AnswerQuestionnaire.css";

//Importacion de componentes de los cuestionarios
import BergScale from "../questionnaire/BergScale";
import EmojiScale from "../questionnaire/EmojiScale";
import ProgressBarScale from "../questionnaire/ProgressBarScale";
import BarthelIndex from "../questionnaire/BarthelIndex";
import McGillPainQuestionnaire from "../questionnaire/McGillPainQuestionnaire";
import SF36Questionnaire from "../questionnaire/SF36Questionnaire";
import PainSelfEfficacyScale from "../questionnaire/PainSelfEfficacyScale";

const AnswerQuestionnaire = () => {
  const {
    questions,
    responses,
    title,
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
    if (questionnaireId !== loadedQuestionnaireId) {
      setSelectedQuestionnaire({ id: questionnaireId });
      setLoadedQuestionnaireId(questionnaireId);
    }
  }, [questionnaireId, loadedQuestionnaireId, setSelectedQuestionnaire]);

  const handleBack = () => {
    navigate("/patient/pending-questionnaires");
  };

  return (
    <div className="answer-questionnaire-container">
      <h1>Responder Cuestionario</h1>
      {title && <h2>{title}</h2>}
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
              {title === "Escala de Autonomía de Berg" &&
              question.type === "scale" ? (
                <BergScale
                  options={question.options}
                  selectedValue={responses[question.id]}
                  onSelect={(value) => handleResponseChange(question.id, value)}
                />
              ) : title === "Índice de Barthel" ? (
                <BarthelIndex
                  question={question}
                  selectedValue={responses[question.id]}
                  onSelect={(value) => handleResponseChange(question.id, value)}
                />
              ) : title === "Escala de expresiones faciales" &&
                question.type === "scale" ? (
                <EmojiScale
                  options={question.options}
                  selectedValue={responses[question.id]}
                  onSelect={(value) => handleResponseChange(question.id, value)}
                />
              ) : title === "Escala visual analógica" &&
                question.type === "scale" ? (
                <ProgressBarScale
                  options={question.options}
                  selectedValue={responses[question.id]}
                  onSelect={(value) => handleResponseChange(question.id, value)}
                />
                ) : title === "Cuestionario de Dolor de McGill" ? (
                  <McGillPainQuestionnaire
                    question={question}
                    selectedValue={responses[question.id]}
                    onSelect={(value) => handleResponseChange(question.id, value)}
                  />
                ) : title === "Cuestionario SF36" ? (
                  <SF36Questionnaire
                    question={question}
                    selectedValue={responses[question.id]}
                    onSelect={(value) => handleResponseChange(question.id, value)}
                  />
                ): title === "Cuestionario de autoeficiencia frente al dolor" &&
                question.type === "scale" ? (
                  <PainSelfEfficacyScale
                    question={question}
                    selectedValue={responses[question.id]}
                    onSelect={(value) => handleResponseChange(question.id, value)}
                  />
              ) : null}
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
