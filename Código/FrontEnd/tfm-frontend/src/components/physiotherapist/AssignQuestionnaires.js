import React from "react";
import useAssignQuestionnaires from "../../hooks/useAssignQuestionnaires";
import "../../styles/AssignQuestionnaires.css";

const AssignQuestionnaires = () => {
  const {
    patients,
    questionnaires,
    selectedPatient,
    setSelectedPatient,
    selectedQuestionnaires,
    setSelectedQuestionnaires,
    handleAssign,
    loading,
    error,
    success,
  } = useAssignQuestionnaires();

  const handleCheckboxChange = (e) => {
    const { value, checked } = e.target;
    const questionnaireId = Number(value);

    if (checked) {
      setSelectedQuestionnaires([...selectedQuestionnaires, questionnaireId]);
    } else {
      setSelectedQuestionnaires(
        selectedQuestionnaires.filter((id) => id !== questionnaireId)
      );
    }
  };

  const isButtonDisabled =
    !selectedPatient || selectedQuestionnaires.length === 0;

  return (
    <div className="assign-questionnaires-container">
      <div className="assign-card">
        <h2 className="assign-title">Asignar Cuestionarios</h2>
        {error && <p className="error-message">{error}</p>}
        {success && <p className="success-message">{success}</p>}
        {loading ? (
          <p className="loading-message">Cargando...</p>
        ) : (
          <>
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
                    {patient.name} {patient.lastname}
                  </option>
                ))}
              </select>
            </div>
            <div className="select-questionnaires">
              <h3>Selecciona Cuestionarios</h3>
              <ul>
                {questionnaires.map((questionnaire) => (
                  <li key={questionnaire.id}>
                    <label>
                      <input
                        type="checkbox"
                        value={questionnaire.id}
                        checked={selectedQuestionnaires.includes(
                          questionnaire.id
                        )}
                        onChange={handleCheckboxChange}
                      />
                      {questionnaire.title}
                    </label>
                  </li>
                ))}
              </ul>
            </div>
            <button
              onClick={handleAssign}
              className="assign-button"
              disabled={isButtonDisabled || loading}
            >
              {loading ? "Asignando..." : "Asignar Cuestionarios"}
            </button>
          </>
        )}
      </div>
    </div>
  );
};

export default AssignQuestionnaires;
