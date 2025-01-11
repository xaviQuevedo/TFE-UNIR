import React from "react";
import { useNavigate } from "react-router-dom";
import "../styles/PhysiotherapistDashboard.css";

const PhysiotherapistDashboard = () => {
  const navigate = useNavigate();

  const handleAssignQuestionnaire = () => {
    navigate("/physiotherapist/assign-questionnaire");
  };
  const handleListCompletedQuestionnaire = () => {
    navigate("/physiotherapist/view-assigned-patients");
  };
  const handleStatistics = () => {
    navigate("/physiotherapist/statistics");
  };
  const handleClinicalHistory = () => {
    navigate("/physiotherapist/clinical-history");
  };
  const handleChangePassword = () => {
    navigate("/physiotherapist/change-password");
  };

  return (
    <div className="physiotherapist-dashboard-container">
      <div className="dashboard-card">
        <h1 className="dashboard-title">Panel del Fisioterapeuta</h1>
        <div className="button-container">
          <button
            className="dashboard-button"
            onClick={handleAssignQuestionnaire}
          >
            Asignar cuestionario
          </button>
          <button
            className="dashboard-button"
            onClick={handleListCompletedQuestionnaire}
          >
            Ver pacientes y respuestas de los cuestionarios
          </button>
          <button className="dashboard-button" onClick={handleStatistics}>
            Ver estadísticas
          </button>
          <button className="dashboard-button" onClick={handleClinicalHistory}>
            Ver historias clínicas
          </button>
          <button className="dashboard-button" onClick={handleChangePassword}>
            Cambiar contraseña
          </button>
        </div>
      </div>
    </div>
  );
};

export default PhysiotherapistDashboard;
