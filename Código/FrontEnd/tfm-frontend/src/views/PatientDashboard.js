import React from "react";
import { useNavigate } from "react-router-dom";
import "../styles/PatientDashboard.css";

const PatientDashboard = () => {
  const navigate = useNavigate();

  const handleViewPendingQuestionnaires = () => {
    navigate("/patient/pending-questionnaires");
  };

  const handleViewProgress = () => {
    navigate("/patient/progress");
  };

  const handleViewCompletedQuestionnaires = () => {
    navigate("/patient/completed-questionnaires");
  };

  return (
    <div className="patient-dashboard-container">
      <h1>Dashboard del Paciente</h1>
      <button onClick={handleViewPendingQuestionnaires}>
        Ver Cuestionarios Pendientes
      </button>
      <button onClick={handleViewProgress}>Ver Progreso</button>
      <button onClick={handleViewCompletedQuestionnaires}>
        Ver Cuestionarios Completados
      </button>
    </div>
  );
};

export default PatientDashboard;
