import React from "react";
import { useNavigate } from "react-router-dom";
import useCompleteQuestionnaires from "../../hooks/useCompleteQuestionnaires";
import "../../styles/CompleteQuestionnaires.css";

const CompleteQuestionnaires = () => {
  const { pendingQuestionnaires, setCurrentQuestionnaire, loading, error } =
    useCompleteQuestionnaires();
  const navigate = useNavigate();

  const handleQuestionnaireSelection = (questionnaire) => {
    setCurrentQuestionnaire(questionnaire);
    navigate(`/patient/answer-questionnaire/${questionnaire.id}`);
  };

  return (
    <div className="complete-questionnaires-container">
      <h1 className="complete-questionnaires-title">Cuestionarios Pendientes</h1>
      {error && <p className="error-message">{error}</p>}
      {loading ? (
        <p className="loading-message">Cargando...</p>
      ) : (
        <ul className="pending-questionnaires-list">
          {pendingQuestionnaires.map((q) => (
            <li
              key={q.id}
              className="questionnaire-item"
              onClick={() => handleQuestionnaireSelection(q)}
            >
              <span className="questionnaire-title">{q.title}</span>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default CompleteQuestionnaires;
