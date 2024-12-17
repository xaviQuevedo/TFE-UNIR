import { useState, useEffect } from "react";
import { physiotherapistService } from "../api/apiConfig";
import { questionnaireService } from "../api/apiConfig";

const useAssignQuestionnaires = () => {
    const [patients, setPatients] = useState([]);
    const [questionnaires, setQuestionnaires] = useState([]);
     const [selectedPatient, setSelectedPatient] = useState("");
    const [selectedQuestionnaires, setSelectedQuestionnaires] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState("");

    // Cargar los pacientes y cuestionarios al iniciar
    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);
                const token = localStorage.getItem("token");
                const physiotherapistId = localStorage.getItem("id"); // Guardar ID al autenticar
                console.log("iddd "+physiotherapistId);

                if (!physiotherapistId) {
                    throw new Error("ID del fisioterapeuta no encontrado.");
                }

                // Obtener los pacientes asignados al fisioterapeuta
                const [patientsResponse, questionnairesResponse] = await Promise.all([
                    physiotherapistService.get(`/assignments/pysiotherapist/${physiotherapistId}`, {
                        headers: { Authorization: `Bearer ${token}` },
                    }),
                    questionnaireService.get("/questionnaires", {
                        headers: { Authorization: `Bearer ${token}` },
                    }), 
                ]);

                console.log("patientsResponse: ", JSON.stringify(patientsResponse.data, null, 2));

                setPatients(patientsResponse.data);
                setQuestionnaires(questionnairesResponse.data);
                setLoading(false);
            } catch (err) {
                setError(err.response?.data?.message || "Error al cargar los datos.");
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    const handleAssign = async () => {
        if (!selectedPatient  || selectedQuestionnaires.length  === 0) {
            setError("Selecciona un paciente y al menos un cuestionario.");
            return;
        }
        try {
            setLoading(true);
            const token = localStorage.getItem("token");

            await physiotherapistService.post(
                "/assignments",
                {
                    patientId: selectedPatient,
                     questionnaireIds: selectedQuestionnaires,
                 },
                {
                    headers: { Authorization: `Bearer ${token}` },
                }
            );

            setSuccess("Cuestionarios asignados correctamente.");
            setSelectedPatient("");
            setSelectedQuestionnaires([]);
            setLoading(false);
        } catch (err) {
            setError(err.response?.data?.message || "Error al asignar cuestionarios.");
            setLoading(false);
        }
    };

    return {
        patients,
        questionnaires,
        selectedPatient,
        setSelectedPatient,
        selectedQuestionnaires,
        setSelectedQuestionnaires, 
        handleAssign,
        error,
        loading,
        success,
    };
};

export default useAssignQuestionnaires;
