import React from "react";

import { useNavigate } from "react-router-dom";
import '../styles/AdminDashboard.css';

const AdminDashboard = () => {

    const navigate = useNavigate();

    const handleCreateUser = () => {
        navigate("/admin/create-user");
    };

    const handleUpdateUser = () => {
        navigate("/admin/update-user");
    };

    const handleAssignPatients = () => {
        navigate("/admin/assign-patients");
    }

    return (
        <div className="admin-dashboard-container">
            <h1>Admin Dashboard</h1>
            <button onClick={handleCreateUser}>Crear un nuevo usuario</button>
            <button onClick={handleUpdateUser}>Actualizar o eliminar usuario</button>
            <button onClick={handleAssignPatients}>Asignar pacientes a un fisioterapeuta</button>
        </div>
    );
};

export default AdminDashboard;