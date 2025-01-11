import React, { useState } from "react"; 
import useViewAssignedPatients from "../../hooks/useViewAssignedPatients";
import "../../styles/ViewAssignedPatients.css";

const ViewAssignedPatients = () => {
  const {
    patients,
    questionnaires,
    selectedPatient,
    setSelectedPatient,
    selectedQuestionnaire,
    setSelectedQuestionnaire,
    responses,
    comments,
    setComments,
    handleSaveComments,
    loading,
    error,
    success,
    fetchResponses,
  } = useViewAssignedPatients();

  const [showModal, setShowModal] = useState(false);

  const isCommentButtonDisabled = !selectedQuestionnaire || !comments.trim();

  const handleSaveWithModal = async () => {
    await handleSaveComments();
    if (success) {
      setShowModal(true); // Muestra el modal al guardar correctamente
    }
  };

  // Formato de fecha en día/mes/año
  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString("es-ES", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
    });
  };

  return (
    <div className="view-assigned-patients-container">
      <div className="view-card">
        <h2 className="view-title">Gestión de Cuestionarios</h2>
        {error && <p className="error-message">{error}</p>}
        {loading ? (
          <p className="loading-message">Cargando...</p>
        ) : (
          <div className="content">
            {showModal && (
              <div className="modal-overlay">
                <div className="modal-content">
                  <h3>Comentario Guardado</h3>
                  <p>El comentario se ha guardado correctamente.</p>
                  <button
                    className="modal-button"
                    onClick={() => setShowModal(false)}
                  >
                    Aceptar
                  </button>
                </div>
              </div>
            )}
            <div className="select-patient">
              <h3>Selecciona un Paciente</h3>
              <select
                value={selectedPatient}
                onChange={(e) => setSelectedPatient(e.target.value)}
                className="select-dropdown"
              >
                <option value="">-- Seleccionar --</option>
                {patients.map((patient) => (
                  <option key={patient.user_id} value={patient.user_id}>
                    {patient.name} {patient.last_name}
                  </option>
                ))}
              </select>
            </div>

            {selectedPatient && (
              <div className="select-questionnaires">
                <h3>Cuestionarios en Progreso</h3>
                <ul className="questionnaire-list">
                  {questionnaires.map((q) => (
                    <li key={q.id} className="questionnaire-item">
                      <label>
                        <input
                          type="radio"
                          value={q.questionnaireId}
                          checked={selectedQuestionnaire === q.questionnaireId}
                          onChange={(e) => {
                            setSelectedQuestionnaire(Number(e.target.value));
                            fetchResponses(Number(e.target.value));
                          }}
                        />
                        <span className="questionnaire-title">{q.questionnaireTitle}</span>
                        <span className={`score-badge ${q.score >= 100 ? "low-score" : "high-score"}`}>
                          {q.score !== null ? `Score: ${q.score}` : "No aplica puntaje"}
                        </span>
                        <span className="questionnaire-date">
                          Fecha respuesta: {formatDate(q.updatedAt)}
                        </span>
                      </label>
                    </li>
                  ))}
                </ul>
              </div>
            )}

            {selectedQuestionnaire && responses.length > 0 && (
              <div className="responses-section">
                <h3>
                  Respuestas del Cuestionario:
                </h3>
                <ul className="response-list">
                  {responses.map((response, index) => (
                    <li key={index} className="response-item">
                      <strong>Pregunta:</strong> {response.questiontext}
                      <br />
                      <strong>Respuesta:</strong> {response.response}
                    </li>
                  ))}
                </ul>
                <textarea
                  placeholder="Agregar comentarios..."
                  value={comments}
                  onChange={(e) => setComments(e.target.value)}
                  className="comments-textarea"
                ></textarea>
                <button
                  onClick={handleSaveWithModal}
                  className="save-comments-button"
                  disabled={isCommentButtonDisabled}
                >
                  Guardar Comentarios
                </button>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default ViewAssignedPatients;
