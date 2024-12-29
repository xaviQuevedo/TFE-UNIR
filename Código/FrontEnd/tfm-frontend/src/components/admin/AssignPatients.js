import React from "react";
import { useNavigate } from "react-router-dom";
import "../../styles/AssignPatients.css";
import useAssignPatients from "../../hooks/useAssignPatients";

const AssignPatients = () => {
  const {
    physiotherapists,
    patients,
    selectedPhysiotherapist,
    setSelectedPhysiotherapist,
    selectedPatients,
    handlePatientSelection,
    handleAssign,
    error,
    loading,
  } = useAssignPatients();

  const navigate = useNavigate();

  if (loading) return <p>Cargando datos...</p>;
  if (error) return <p>Error al cargar los datos: {error.message}</p>;

  return (
    <div className="assign-patients-container">
      <div className="assign-patients-card">
        <h1>Asignar Pacientes a un Fisioterapeuta</h1>

        <div className="form-group">
          <label htmlFor="physiotherapist">Seleccionar Fisioterapeuta:</label>
          <select
            id="physiotherapist"
            value={selectedPhysiotherapist}
            onChange={(e) => setSelectedPhysiotherapist(e.target.value)} // Cambiar fisioterapeuta
          >
            <option value="">-- Selecciona un fisioterapeuta --</option>
            {physiotherapists.map((physiotherapist) => (
              <option
                key={physiotherapist.user_id}
                value={physiotherapist.user_id}
              >
                {physiotherapist.name} {physiotherapist.last_name}
              </option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label htmlFor="patients">Seleccionar Pacientes:</label>
          <ul className="patients-list">
            {patients.map((patient) => (
              <li key={patient.id}>
                <label>
                  <input
                    type="checkbox"
                    value={patient.id}
                    onChange={() => handlePatientSelection(patient.user_id)}
                    checked={selectedPatients.includes(patient.user_id)}
                  />
                  {patient.name} {patient.last_name}
                </label>
              </li>
            ))}
          </ul>
        </div>

        <button
          className="assign-button"
          onClick={() => handleAssign(navigate)}
          disabled={!selectedPhysiotherapist || selectedPatients.length === 0}
        >
          Asignar Pacientes
        </button>
      </div>
    </div>
  );
};

export default AssignPatients;
