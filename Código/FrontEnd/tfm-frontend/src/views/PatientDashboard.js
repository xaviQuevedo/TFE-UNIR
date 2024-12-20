import React from "react";
import { useNavigate } from "react-router-dom";
import "../styles/PatientDashboard.css";

const PatientDasboard = () => {
    const navigate = useNavigate();
    
    const handleViewPendingQuestionnaires = () => {
        navigate("/patient/pending-questionnaires");
    };

    return (
        <div className="patient-dashboard-conatiner">
            <h1>Dashboard del paciente</h1>
            <button onClick={handleViewPendingQuestionnaires}>
                Ver cuestionarios pendientes
            </button>
        </div>
    );
};

export default PatientDasboard;