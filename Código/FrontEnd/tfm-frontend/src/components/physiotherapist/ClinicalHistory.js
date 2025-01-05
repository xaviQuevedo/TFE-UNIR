import React, { useState, useEffect } from "react";
import { physiotherapistService, questionnaireService } from "../../api/apiConfig";
import "../../styles/ClinicalHistory.css";

const ClinicalHistory = () => {
  const [patients, setPatients] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchPatients = async () => {
      try {
        setLoading(true);
        const token = localStorage.getItem("token");
        const physiotherapistId = localStorage.getItem("id");

        const response = await physiotherapistService.get(
          `/assignments/physiotherapist/${physiotherapistId}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        setPatients(response.data);
      } catch (err) {
        setError("Error al cargar los pacientes.");
      } finally {
        setLoading(false);
      }
    };

    fetchPatients();
  }, []);

  const handleGeneratePdf = async (patient) => {
    try {
      setLoading(true);
      const token = localStorage.getItem("token");

      const response = await questionnaireService.get(`/clinicals-histories/${patient.user_id}/pdf`, {
        headers: { Authorization: `Bearer ${token}` },
        responseType: "blob", // Manejo de archivos binarios
      });

      // Crear enlace para descargar el archivo
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;

      // Construir el nombre del archivo usando nombre y apellido del paciente
      const sanitizedName = `${patient.name}_${patient.last_name}`
        .replace(/\s+/g, "_") // Reemplazar espacios por guion bajo
        .replace(/[^a-zA-Z0-9_]/g, ""); // Eliminar caracteres no válidos
      link.setAttribute("download", `historia_clinica_${sanitizedName}.pdf`);

      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    } catch (err) {
      console.error("Error al generar el PDF:", err);
      setError("Error al generar la historia clínica.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="clinical-history-dashboard">
      <h2 className="dashboard-title">Pacientes Asignados</h2>
      {loading && <p className="loading-message">Cargando...</p>}
      {error && <p className="error-message">{error}</p>}
      {!loading && !error && patients.length === 0 && (
        <p className="no-patients-message">No hay pacientes asignados.</p>
      )}
      <ul className="patient-list">
        {patients.map((patient) => (
          <li key={patient.user_id} className="patient-item">
            <div>
              <strong>Paciente:</strong> {patient.name} {patient.last_name}
            </div>
            <button
              className="generate-pdf-button"
              onClick={() => handleGeneratePdf(patient)}
              disabled={loading}
            >
              Generar Historia Clínica
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ClinicalHistory;
