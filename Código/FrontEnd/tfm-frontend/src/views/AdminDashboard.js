import React from "react";
import { useNavigate } from "react-router-dom";
import "../styles/AdminDashboard.css";

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
  };

  return (
    <div className="admin-dashboard-container">
      <div className="admin-dashboard-card">
        <h1 className="admin-dashboard-title">Panel de Administración</h1>
        <div className="admin-dashboard-buttons">
          <button
            className="admin-dashboard-button"
            onClick={handleCreateUser}
          >
            Crear un nuevo usuario
          </button>
          <button
            className="admin-dashboard-button"
            onClick={handleUpdateUser}
          >
            Eliminar usuario
          </button>
          <button
            className="admin-dashboard-button"
            onClick={handleAssignPatients}
          >
            Asignar pacientes a un fisioterapeuta
          </button>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
