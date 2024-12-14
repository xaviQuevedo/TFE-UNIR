import React, { useState } from "react";
import useUserManagement from "../../hooks/useUserManagement";
import "../../styles/UserTable.css";

const UserTable = () => {
  const { users, loading, deleteUser, updateUser } = useUserManagement();
  const [editedUser, setEditedUser] = useState({});

  // Handle change in editable fields
  const handleChange = (e, userId, field) => {
    const newValue = e.target.value;
    setEditedUser((prev) => ({
      ...prev,
      [userId]: { ...prev[userId], [field]: newValue },
    }));
  };

  // Handle save changes
  const handleUpdate = (userId) => {
    if (editedUser && editedUser[userId]) {
      updateUser(userId, editedUser[userId]);
      setEditedUser((prev) => {
        const newState = { ...prev };
        delete newState[userId];
        return newState;
      });
    }
  };

  return (
    <div className="user-table-container">
      <h2>Usuarios Registrados</h2>
      {loading && <p>Cargando...</p>}
      <table className="user-table">
        <thead>
          <tr>
            <th>Email</th>
            <th>Rol</th>
            <th>Acción</th>
          </tr>
        </thead>
        <tbody>
          {users && users.length > 0 ? (
            users.map((user) => (
              <tr key={user.user_id}>
                <td>
                  <input
                    type="text"
                    value={editedUser[user.user_id]?.email || user.email}
                    onChange={(e) => handleChange(e, user.user_id, "email")}
                  />
                </td>
                <td>
                  <select
                    value={editedUser[user.user_id]?.role || user.role}
                    onChange={(e) => handleChange(e, user.user_id, "role")}
                  >
                    <option value="admin">Administrador</option>
                    <option value="patient">Paciente</option>
                    <option value="physiotherapy">Fisioterapeuta</option>
                  </select>
                </td>
                <td>
                  <button
                    className="update"
                    onClick={() => handleUpdate(user.user_id)}
                  >
                    Actualizar
                  </button>
                  <button
                    className="delete"
                    onClick={() => deleteUser(user.user_id)}
                  >
                    Eliminar
                  </button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="3">No hay usuarios disponibles.</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default UserTable;
