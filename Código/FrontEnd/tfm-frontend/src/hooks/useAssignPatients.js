import { useState, useEffect } from "react"; 
import { physiotherapistService } from "../api/apiConfig";

const useAssignPatients = () => {
  const [physiotherapists, setPhysiotherapists] = useState([]); 
  const [patients, setPatients] = useState([]); // Lista dinámica de pacientes no asignados
  const [selectedPhysiotherapist, setSelectedPhysiotherapist] = useState("");
  const [selectedPatients, setSelectedPatients] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  // Obtener fisioterapeutas
  useEffect(() => {
    const fetchPhysiotherapists = async () => {
      try {
        setLoading(true);
        const token = localStorage.getItem("token");
        const response = await physiotherapistService.get("/assignments/physiotherapists", {
          headers: { Authorization: `Bearer ${token}` },
        });
        setPhysiotherapists(response.data);
        setLoading(false);
      } catch (error) {
        setError(error);
        setLoading(false);
        alert("Error al cargar fisioterapeutas: " + error.message);
      }
    };

    fetchPhysiotherapists();
  }, []);

  // Obtener pacientes no asignados para el fisioterapeuta seleccionado
  const fetchUnassignedPatients = async (physiotherapistId) => {
    try {
      setLoading(true);
      const token = localStorage.getItem("token");
      const response = await physiotherapistService.get(
        `/assignments/physiotherapists/${physiotherapistId}/unassigned-patients`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setPatients(response.data);
      setLoading(false);
    } catch (error) {
      setError(error);
      setLoading(false);
      alert("Error al cargar pacientes no asignados: " + error.message);
    }
  };

  // Manejar cambio de fisioterapeuta
  const handlePhysiotherapistChange = async (id) => {
    setSelectedPhysiotherapist(id);
    setSelectedPatients([]); // Limpiar selección de pacientes
    if (id) {
      await fetchUnassignedPatients(id); // Actualizar pacientes disponibles
    } else {
      setPatients([]); // Vaciar lista de pacientes si no hay fisioterapeuta seleccionado
    }
  };
  
  // Manejar asignación de pacientes
  const handleAssign = async (navigate) => {
    if (!selectedPhysiotherapist || selectedPatients.length === 0) {
      alert("Por favor, selecciona un fisioterapeuta y al menos un paciente.");
      return;
    }

    try {
      const token = localStorage.getItem("token");
      const payload = {
        physiotherapistId: selectedPhysiotherapist,
        patientIds: selectedPatients,
      };

      await physiotherapistService.post(
        "/assignments",
        payload,
        { headers: { Authorization: `Bearer ${token}` } }
      );

      alert("Pacientes asignados correctamente.");
      setSelectedPhysiotherapist("");
      setSelectedPatients([]);
      navigate("/admin/dashboard");
    } catch (error) {
      setError(error);

      if (error.response && error.response.status === 409) {
        alert("Ya existe una asignación entre el paciente seleccionado y este fisioterapeuta.");
        if (selectedPhysiotherapist) {
          await fetchUnassignedPatients(selectedPhysiotherapist); // Refrescar pacientes disponibles
        }
      } else {
        alert("Hubo un error al asignar los pacientes.");
      }
    }
  };

  // Manejar selección de pacientes
  const handlePatientSelection = (patientId) => {
    if (selectedPatients.includes(patientId)) {
      setSelectedPatients(selectedPatients.filter((id) => id !== patientId));
    } else {
      setSelectedPatients([...selectedPatients, patientId]);
    }
  };

  return {
    physiotherapists,
    patients,
    selectedPhysiotherapist,
    setSelectedPhysiotherapist: handlePhysiotherapistChange, // Cambiar fisioterapeuta y cargar pacientes
    selectedPatients,
    handlePatientSelection,
    handleAssign,
    error,
    loading,
  };
};

export default useAssignPatients;
