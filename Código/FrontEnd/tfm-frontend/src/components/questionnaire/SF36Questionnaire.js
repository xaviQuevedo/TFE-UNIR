import React from "react";
import "../../styles/questionnaires/SF36Questionnaire.css";

const SF36Questionnaire = ({ question, selectedValue, onSelect }) => {
  const renderOptions = () => {
    if (Array.isArray(question.options)) {
      return question.options.map((option, index) => (
        <button
          key={index}
          className={`sf36-option ${selectedValue === option ? "selected" : ""}`}
          onClick={() => onSelect(option)}
        >
          {option}
        </button>
      ));
    } else {
      return Object.entries(question.options).map(([key, value]) => (
        <button
          key={key}
          className={`sf36-option ${selectedValue === key ? "selected" : ""}`}
          onClick={() => onSelect(key)}
        >
          <span className="sf36-option-text">{value}</span>
        </button>
      ));
    }
  };

  return (
    <div className="sf36-question-container">
      <div className="sf36-options-container">{renderOptions()}</div>
    </div>
  );
};

export default SF36Questionnaire;
