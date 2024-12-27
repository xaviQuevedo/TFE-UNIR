import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { patientService } from "../../api/apiConfig";
//import "../styles/CompletedQuestionnaires.css";

const CompletedQuestionnaires = () => {
  const [completedQuestionnaires, setCompletedQuestionnaires] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchCompletedQuestionnaires = async () => {
      try {
        setLoading(true);
        const token = localStorage.getItem("token");
        const patientId = localStorage.getItem("id");

        const response = await patientService.get(
          `/patients/${patientId}/completed-questionnaires`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        setCompletedQuestionnaires(response.data);
      } catch (err) {
        console.error("Error al cargar cuestionarios completados:", err);
        setError("No se pudieron cargar los cuestionarios completados.");
      } finally {
        setLoading(false);
      }
    };

    fetchCompletedQuestionnaires();
  }, []);

  const handleViewResponses = (questionnaireId) => {
    navigate(`/patient/completed-questionnaire/${questionnaireId}`);
  };

  return (
    <div className="completed-questionnaires-container">
      <h1>Cuestionarios Completadosss</h1>
      {loading && <p>Cargando cuestionarios...</p>}
      {error && <p className="error-message">{error}</p>}
      <ul className="completed-questionnaires-list">
        {completedQuestionnaires.map((questionnaire) => (
          <li key={questionnaire.id}>
            <span>{questionnaire.title}</span>
            <button onClick={() => handleViewResponses(questionnaire.id)}>
              Ver respuestas
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default CompletedQuestionnaires;
