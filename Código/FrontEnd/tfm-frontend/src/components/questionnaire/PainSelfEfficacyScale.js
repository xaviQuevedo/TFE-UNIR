import React from "react";
import "../../styles/questionnaires/PainSelfEfficacyScale.css";

const PainSelfEfficacyScale = ({ question, selectedValue, onSelect }) => {
  const renderOptions = () => {
    return Object.entries(question.options).map(([key, value]) => (
      <button
        key={key}
        className={`self-efficacy-option ${
          selectedValue === key ? "selected" : ""
        }`}
        onClick={() => onSelect(key)}
      >
        <span className="self-efficacy-option-text">{value}</span>
      </button>
    ));
  };

  return (
    <div className="self-efficacy-question-container">
      <div className="self-efficacy-options-container">{renderOptions()}</div>
    </div>
  );
};

export default PainSelfEfficacyScale;
