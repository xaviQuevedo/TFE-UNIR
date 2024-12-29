import React from "react";
import { useNavigate } from "react-router-dom";
import "../styles/PhysiotherapistDashboard.css";

const PhysiotherapistDashboard = () => {
  const navigate = useNavigate();

  const handleAssignQuestionnaire = () => {
    navigate("/physiotherapist/assign-questionnaire");
  };

  return (
    <div className="physiotherapist-dashboard-container">
      <div className="dashboard-card">
        <h1 className="dashboard-title">Dashboard del Fisioterapeuta</h1>
        <button className="dashboard-button" onClick={handleAssignQuestionnaire}>
          Asignar cuestionario
        </button>
      </div>
    </div>
  );
};

export default PhysiotherapistDashboard;
