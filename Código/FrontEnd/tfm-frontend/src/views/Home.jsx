import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/Home.css';

const Home = () => {
    return (
        <div className="home-container">
            <div className="home-content">
                <h1 className="home-title">Bienvenido</h1>
                <p className="home-description">Sistema para registrar avances en fisioterapia</p>
                <p className="home-subtext">Por favor, inicie sesión para continuar</p>
                <nav>
                    <Link to="/login" className="home-button">
                        Iniciar Sesión
                    </Link>
                </nav>
            </div>
        </div>
    );
};

export default Home;
