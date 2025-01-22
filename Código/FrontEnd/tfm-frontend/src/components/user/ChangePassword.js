import React, { useState } from "react";
import { userService } from "../../api/apiConfig"; // Asegúrate de que esta es tu API configurada
import "../../styles/ChangePassword.css"; // Crea un archivo CSS para estilizar este componente

const ChangePassword = () => {
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleChangePassword = async () => {
    if (newPassword !== confirmPassword) {
      setError("Las contraseñas no coinciden");
      return;
    }

    try {
      setLoading(true);
      const userId = localStorage.getItem("id"); // Suponiendo que el ID del usuario está en el localStorage
      const token = localStorage.getItem("token");

      const response = await userService.patch(
        `/users/${userId}`,
        { currentPassword, newPassword },
        { headers: { Authorization: `Bearer ${token}` } }
      );

      setMessage(response.data.message);
      setError(null);
    } catch (err) {
      setError(err.response?.data?.message || "Error al cambiar la contraseña");
      setMessage(null);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="change-password-container">
      <h2>Cambiar Contraseña</h2>
      {message && <p className="success-message">{message}</p>}
      {error && <p className="error-message">{error}</p>}

      <div className="form-group">
        <label>Contraseña Actual</label>
        <input
          type="password"
          value={currentPassword}
          onChange={(e) => setCurrentPassword(e.target.value)}
          placeholder="Introduce tu contraseña actual"
        />
      </div>

      <div className="form-group">
        <label>Nueva Contraseña</label>
        <input
          type="password"
          value={newPassword}
          onChange={(e) => setNewPassword(e.target.value)}
          placeholder="Introduce tu nueva contraseña"
        />
      </div>

      <div className="form-group">
        <label>Confirmar Nueva Contraseña</label>
        <input
          type="password"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
          placeholder="Confirma tu nueva contraseña"
        />
      </div>

      <button
        className="change-password-button"
        onClick={handleChangePassword}
        disabled={loading}
      >
        {loading ? "Cambiando..." : "Cambiar Contraseña"}
      </button>
    </div>
  );
};

export default ChangePassword;
