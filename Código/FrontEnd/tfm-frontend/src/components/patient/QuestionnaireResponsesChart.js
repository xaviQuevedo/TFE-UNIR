import React from "react";
import { Line } from "react-chartjs-2";
import {
  Chart as ChartJS,
  LineElement,
  CategoryScale,
  LinearScale,
  PointElement,
  Tooltip,
  Legend,
  Filler,
} from "chart.js";
import useQuestionnaireResponses from "../../hooks/useQuestionnaireResponses";
import "../../styles/QuestionnaireResponsesChart.css";

// Registrar los elementos necesarios
ChartJS.register(
  LineElement,
  CategoryScale,
  LinearScale,
  PointElement,
  Tooltip,
  Legend,
  Filler
);

const QuestionnaireResponsesChart = ({ patientId, questionnaireId }) => {
  const { responses, loading, error } = useQuestionnaireResponses(
    patientId,
    questionnaireId
  );

  if (loading) return <p className="loading-message">Cargando respuestas...</p>;
  if (error) return <p className="error-message">{error}</p>;

  if (!responses || responses.length === 0)
    return (
      <p className="no-data-message">
        No hay datos de puntuación disponibles para este cuestionario.
      </p>
    );

  // Formatear las fechas a día/mes/año
  const labels = responses.map((entry) =>
    new Date(entry.date).toLocaleDateString("es-ES", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
    })
  );
  const scores = responses.map((entry) => entry.score);

  const data = {
    labels,
    datasets: [
      {
        label: "Progreso del Score",
        data: scores,
        backgroundColor: "rgba(102, 187, 106, 0.2)", // Verde claro
        borderColor: "rgba(56, 142, 60, 1)", // Verde fuerte
        borderWidth: 3,
        pointRadius: 6,
        pointHoverRadius: 10,
        tension: 0.4,
        fill: true,
      },
    ],
  };

  const options = {
    responsive: true,
    plugins: {
      legend: {
        display: true,
        position: "top",
        labels: {
          color: "#37474F", // Texto oscuro
          font: {
            size: 14,
            weight: "bold",
          },
        },
        onClick: null,
      },
      tooltip: {
        backgroundColor: "rgba(0, 0, 0, 0.8)",
        titleColor: "#fff",
        bodyColor: "#fff",
        borderWidth: 1,
        borderColor: "rgba(255, 255, 255, 0.5)",
        cornerRadius: 8,
      },
    },
    scales: {
      x: {
        title: {
          display: true,
          text: "Fechas",
          color: "#37474F",
          font: {
            size: 14,
            weight: "bold",
          },
        },
        ticks: {
          color: "#37474F",
        },
      },
      y: {
        title: {
          display: true,
          text: "Score",
          color: "#37474F",
          font: {
            size: 14,
            weight: "bold",
          },
        },
        ticks: {
          color: "#37474F",
        },
      },
    },
  };

  return (
    <div className="chart-container-wrapper">
      <div className="chart-container">
        <h2 className="chart-title">Progreso</h2>
        <Line data={data} options={options} />
      </div>
    </div>
  );
};




export default QuestionnaireResponsesChart;
