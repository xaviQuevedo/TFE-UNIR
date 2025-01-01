import React from "react";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  ArcElement,
  LineElement,
  PointElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import { Doughnut, Bar, Line } from "react-chartjs-2";
import useStatistics from "../../hooks/useStatistics";
import "../../styles/Statistics.css";

// Registrar los elementos necesarios en Chart.js
ChartJS.register(
  CategoryScale, // Escalas para los ejes X/Y
  LinearScale, // Escala lineal
  BarElement, // Elemento para gráficos de barras
  ArcElement, // Elemento para gráficos circulares (Doughnut/Pie)
  LineElement, // Elemento para gráficos de línea
  PointElement, // Puntos en gráficos de línea
  Title, // Títulos en gráficos
  Tooltip, // Tooltips
  Legend // Leyendas
);

const Statistics = () => {
  const { generalStats, completedByPatient, completionRates, loading, error } = useStatistics();

  // Datos para la gráfica de Doughnut
  const doughnutData = {
    labels: ["Completados", "En progreso", "Pendientes"],
    datasets: [
      {
        label: "Distribución de Cuestionarios",
        data: [
          generalStats.totalCompleted || 0,
          generalStats.totalInProgress || 0,
          generalStats.totalPending || 0,
        ],
        backgroundColor: ["#4caf50", "#ffeb3b", "#f44336"],
      },
    ],
  };

  // Datos para la gráfica de Cuestionarios Completados por Paciente
  const barDataByPatient = {
    labels: completedByPatient.map((item) => `Paciente ${item.patientid}`),
    datasets: [
      {
        label: "Cuestionarios Completados",
        data: completedByPatient.map((item) => item.completedcount),
        backgroundColor: "#4caf50",
      },
    ],
  };

  // Datos para la gráfica de Tasas de Completado
  const lineDataCompletionRates = {
    labels: Object.keys(completionRates),
    datasets: [
      {
        label: "Tasas de Completado (%)",
        data: Object.values(completionRates).map((rate) => rate.toFixed(2)),
        borderColor: "#2196f3",
        backgroundColor: "rgba(33, 150, 243, 0.2)",
        fill: true,
        tension: 0.4, // Curvatura en la línea
      },
    ],
  };

  const barOptions = {
    responsive: true,
    plugins: {
      legend: {
        position: "top",
      },
      tooltip: {
        callbacks: {
          label: (context) => {
            const value = context.raw;
            return context.dataset.label === "Tasas de Completado (%)"
              ? `${context.dataset.label}: ${value}%`
              : `${context.dataset.label}: ${value}`;
          },
        },
      },
    },
    scales: {
      y: {
        beginAtZero: true,
        title: {
          display: true,
          text: "Valores",
        },
      },
      x: {
        title: {
          display: true,
          text: "Pacientes",
        },
      },
    },
  };

  return (
    <div className="statistics-dashboard">
      <h2 className="dashboard-title">Estadísticas Generales</h2>
      {loading && <p className="loading-message">Cargando...</p>}
      {error && <p className="error-message">{error}</p>}
      {!loading && !error && (
        <div className="chart-container">
          <div className="chart-item">
            <h3>Distribución de Cuestionarios</h3>
            <Doughnut data={doughnutData} />
          </div>

          <div className="chart-item">
            <h3>Cuestionarios Completados por Paciente</h3>
            <Bar data={barDataByPatient} options={barOptions} />
          </div>

          <div className="chart-item">
            <h3>Tasas de Completado por Paciente</h3>
            <Line data={lineDataCompletionRates} />
          </div>
        </div>
      )}
    </div>
  );
};

export default Statistics;
