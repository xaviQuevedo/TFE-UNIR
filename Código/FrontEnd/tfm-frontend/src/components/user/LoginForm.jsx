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
    } else if (role === 'physiotherapy') {
      navigate('/physiotherapy');
    }
  };

  return (
    <div className="login-container">
      <form className="login-form" onSubmit={handleSubmit}>
        <h1>Login</h1>
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button type="submit" disabled={loading}>
          {loading ? "Loading..." : "Login"}
        </button>
      </form>
      {status && <p>{status}</p>}
    </div>
  );
};

export default Login;
