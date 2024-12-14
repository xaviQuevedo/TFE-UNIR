import React from "react";
import useRegister from "../../hooks/useRegister";
import '../../styles/RegisterForm.css';

const RegisterForm = () => { 
    const { register, status, loading } = useRegister();
    const [email, setEmail] = React.useState('');
    const [password, setPassword] = React.useState('');
    const [role, setRole] = React.useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        register({ email, password, role });
    };

    return (
        <div className="register-container">
            <h1>Register</h1>
            <form className="register-form" onSubmit={handleSubmit}>
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
                <select
                    value={role}
                    onChange={(e) => setRole(e.target.value)}
                >
                    <option value="admin">Administrador</option>
                    <option value="patient">Paciente</option>
                    <option value= "physiotherapy">Fisioterapeuta</option>
                </select>
                <button type="submit" disabled={loading}>
                    {loading ? "Registering..." : "Register"}
                </button>
                {status && <p>{status}</p>}
            </form>
        </div>
    );

};

export default RegisterForm;