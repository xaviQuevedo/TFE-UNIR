import React from "react";
import { useNavigate } from "react-router-dom";
import useCompletedQuestionnaires from "../../hooks/useCompletedQuestionnaires";
import "../../styles/CompletedQuestionnaires.css";

const CompletedQuestionnaires = () => {
  const { completedQuestionnaires, loading, error } = useCompletedQuestionnaires();
  const navigate = useNavigate();

  const handleViewResponses = (questionnaireId) => {
    navigate(`/patient/completed-questionnaire/${questionnaireId}`);
  };

  return (
    <div className="completed-questionnaires-container">
      <div className="completed-questionnaires-card">
        <h1 className="completed-title">Cuestionarios Completados</h1>
        {loading && <p className="loading-message">Cargando cuestionarios...</p>}
        {error && <p className="error-message">{error}</p>}
        <ul className="completed-questionnaires-list">
          {completedQuestionnaires.map((questionnaire) => (
            <li key={questionnaire.id} className="questionnaire-item">
              <span className="questionnaire-title">{questionnaire.title}</span>
              <button
                className="view-responses-button"
                onClick={() => handleViewResponses(questionnaire.id)}
              >
                Ver respuestas
              </button>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default CompletedQuestionnaires;
