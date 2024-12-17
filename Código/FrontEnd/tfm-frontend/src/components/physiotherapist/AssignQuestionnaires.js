import React from "react";
import useAssignQuestionnaires from "../../hooks/useAssignQuestionnaires";
import "../../styles/AssignQuestionnaires.css";

const AssignQuestionnaires = () => {
    const {
        patients,
/*         questionnaires,
 */        selectedPatient,
        setSelectedPatient,
        /* selectedQuestionnaires,
        setSelectedQuestionnaires, */
        handleAssign,
        loading,
        error,
        success, 
    } = useAssignQuestionnaires();
    
    return ( 
        <div className="assign-questionnaires-container">
      <h2>Asignar Cuestionarios</h2>
      {error && <p className="error-message">{error}</p>}
      {success && <p className="success-message">{success}</p>}
      {loading ? (
        <p>Cargando...</p>
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
          {/* <div className="select-questionnaires">
            <h3>Selecciona Cuestionarios</h3>
            <ul>
              {questionnaires.map((questionnaire) => (
                <li key={questionnaire.id}>
                  <label>
                    <input
                      type="checkbox"
                      value={questionnaire.id}
                      checked={selectedQuestionnaires.includes(questionnaire.id)}
                      onChange={(e) => {
                        const { value, checked } = e.target;
                        if (checked) {
                          setSelectedQuestionnaires([...selectedQuestionnaires, value]);
                        } else {
                          setSelectedQuestionnaires(
                            selectedQuestionnaires.filter((id) => id !== value)
                          );
                        }
                      }}
                    />
                    {questionnaire.title}
                  </label>
                </li>
              ))}
            </ul>
          </div> */}
          <button onClick={handleAssign} className="assign-button" disabled={loading}>
            {loading ? "Asignando..." : "Asignar Cuestionarios"}
          </button>
        </>
      )}
    </div>
    );
};

export default AssignQuestionnaires;