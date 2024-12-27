import React from "react";
import usePatientProgress from "../../hooks/usePatientProgress";

const PatientProgress = ({ patientId, questionnaireId }) => {
  const { progress, loading, error } = usePatientProgress(patientId, questionnaireId);

  if (loading) return <p>Cargando progreso...</p>;
  if (error) return <p>{error}</p>;

  return (
    <div>
      <h2>Progreso del Paciente</h2>
      {progress !== null ? (
        <p>El progreso del paciente es: {progress}%</p>
      ) : (
        <p>No se pudo obtener el progreso del paciente.</p>
      )}
    </div>
  );
};

export default PatientProgress;