import React from "react";
import useUserManagement from "../../hooks/useUserManagement";
import "../../styles/UserTable.css";

const UserTable = () => {
  const { users, loading, deleteUser } = useUserManagement();

  return (
    <div className="user-table-container">
      <h2>Usuarios Registrados</h2>
      {loading && <p>Cargando...</p>}
      <table className="user-table">
        <thead>
          <tr>
            <th>Nombre</th>
            <th>Apellido</th>
            <th>Email</th>
            <th>Rol</th>
            <th>Acción</th>
          </tr>
        </thead>
        <tbody>
          {users && users.length > 0 ? (
            users
              .filter((user) => user.role !== "admin") // Filtrar administradores
              .map((user) => (
                <tr key={user.user_id}>
                  <td>{user.name}</td>
                  <td>{user.last_name}</td>
                  <td>{user.email}</td>
                  <td>{user.role}</td>
                  <td>
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
              <td colSpan="5">No hay usuarios disponibles.</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default UserTable;
