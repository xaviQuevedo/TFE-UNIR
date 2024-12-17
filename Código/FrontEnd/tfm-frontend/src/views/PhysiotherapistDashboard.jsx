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
      <h1>Dashboard del Fisioterapeuta</h1>
      <button onClick={handleAssignQuestionnaire}>Asignar cuestionario</button>
    </div>
  );
};

export default PhysiotherapistDashboard;
