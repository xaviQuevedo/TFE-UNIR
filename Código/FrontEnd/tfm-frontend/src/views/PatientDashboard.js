import React from "react";
import { useNavigate } from "react-router-dom";
import "../styles/PatientDashboard.css";

const PatientDashboard = () => {
  const navigate = useNavigate();

  const handleViewPendingQuestionnaires = () => {
    navigate("/patient/pending-questionnaires");
  };

  const handleViewCompletedQuestionnaires = () => {
    navigate("/patient/completed-questionnaires");
  };

  return (
    <div className="admin-dashboard-container">
      <div className="admin-dashboard-card">
        <h1 className="admin-dashboard-title">Dashboard del Paciente</h1>
        <p>Gestiona tus cuestionarios y monitorea tu progreso.</p>
        <div className="admin-dashboard-buttons">
          <button
            className="admin-dashboard-button"
            onClick={handleViewPendingQuestionnaires}
          >
            Ver Cuestionarios Pendientes
          </button>
          <button
            className="admin-dashboard-button"
            onClick={handleViewCompletedQuestionnaires}
          >
            Ver progreso por cuestionario
          </button>
        </div>
      </div>
    </div>
  );
};

export default PatientDashboard;
