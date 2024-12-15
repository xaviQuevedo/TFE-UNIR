import React, { useState } from "react";
import useLogin from "../../hooks/useLogin";
import '../../styles/LoginForm.css';
import { useNavigate } from "react-router-dom";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const { login, status, loading } = useLogin();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    const role = await login({ email, password });
    if (role === 'admin') {
      navigate('/admin/dashboard');
    } else if (role === 'patient') {
      navigate('/patient');
    } else if (role === 'physiotherapist') {
      navigate('/physiotherapist');
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <h1 className="login-title">Iniciar Sesión</h1>
        <form className="login-form" onSubmit={handleSubmit}>
          <input
            type="email"
            placeholder="Correo electrónico"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="login-input"
          />
          <input
            type="password"
            placeholder="Contraseña"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="login-input"
          />
          <button type="submit" disabled={loading} className="login-button">
            {loading ? "Cargando..." : "Acceder"}
          </button>
        </form>
        {status && <p className="login-status">{status}</p>}
      </div>
    </div>
  );
};

export default Login;
