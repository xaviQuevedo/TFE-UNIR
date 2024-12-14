import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/Home.css';

const Home = () => {
    return (
        <div className="home-container">
            <h1>Bienvenido al sistema para registrar avances en fisioterapia</h1>
            <p>Por favor, seleccione una opción:</p>
            <nav>
                <ul> 
                <li><Link to="/login">Iniciar sesión</Link></li>
                <li><Link to="/register">Registrarse</Link></li>

                </ul>
            </nav>
        </div>
    );
};

export default Home;