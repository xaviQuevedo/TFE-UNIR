import React from "react";
import useRegister from "../../hooks/useRegister";
import "../../styles/RegisterForm.css";

const RegisterForm = () => {
  const { register, loading, showConfirmation, handleRedirect } = useRegister();
  const [email, setEmail] = React.useState("");
  const [password, setPassword] = React.useState("");
  const [role, setRole] = React.useState("patient");
  const [name, setName] = React.useState("");
  const [last_name, setLastName] = React.useState("");
  const [error, setError] = React.useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validar que todos los campos estén llenos
    if (!email || !password || !name || !last_name || !role) {
      setError("Todos los campos son obligatorios.");
      return;
    }

    setError(""); // Limpiar el mensaje de error
    register({ email, password, role, name, last_name });
  };

  return (
    <div className="register-container">
      <div className="register-card">
        <h1 className="register-title">Registrar Usuario</h1>
        <form className="register-form" onSubmit={handleSubmit}>
          <input
            className="register-input"
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <input
            className="register-input"
            type="password"
            placeholder="Contraseña"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <input
            className="register-input"
            type="text"
            placeholder="Nombre"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
          <input
            className="register-input"
            type="text"
            placeholder="Apellido"
            value={last_name}
            onChange={(e) => setLastName(e.target.value)}
          />
          <select
            className="register-select"
            value={role}
            onChange={(e) => setRole(e.target.value)}
          >
            <option value="patient">Paciente</option>
            <option value="physiotherapist">Fisioterapeuta</option>
          </select>
          {error && <p className="error-message">{error}</p>}
          <button className="register-button" type="submit" disabled={loading}>
            {loading ? "Registrando..." : "Registrar"}
          </button>
        </form>
      </div>
      {showConfirmation && (
        <>
          <div className="modal-overlay"></div>
          <div className="confirmation-modal">
            <p>Usuario registrado exitosamente.</p>
            <button onClick={handleRedirect}>Aceptar</button>
          </div>
        </>
      )}
    </div>
  );
};

export default RegisterForm;
