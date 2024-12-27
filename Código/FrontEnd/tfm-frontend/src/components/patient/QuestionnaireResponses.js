import React from "react";
import { useParams } from "react-router-dom";
import QuestionnaireResponsesChart from "./QuestionnaireResponsesChart";
//import "../styles/QuestionnaireResponses.css";

const QuestionnaireResponses = () => {
  const { questionnaireId } = useParams();
  const patientId = localStorage.getItem("id");

  return (
    <div className="questionnaire-responses-container">
      <QuestionnaireResponsesChart patientId={patientId} questionnaireId={questionnaireId} />
    </div>
  );
};

export default QuestionnaireResponses;
