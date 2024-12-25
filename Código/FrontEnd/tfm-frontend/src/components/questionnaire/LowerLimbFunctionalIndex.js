import React from "react";
import "../../styles/LowerLimbFunctionalIndex.css";

const LowerLimbFunctionalIndex = ({ question, selectedValue, onSelect }) => {
  const renderOptions = () => {
    return Object.entries(question.options).map(([key, value]) => (
      <button
        key={key}
        className={`lower-limb-option ${
          selectedValue === key ? "selected" : ""
        }`}
        onClick={() => onSelect(key)}
      >
        <span className="lower-limb-option-score">{key}</span>
        <span className="lower-limb-option-text">{value}</span>
      </button>
    ));
  };

  return (
    <div className="lower-limb-question-container">
      <div className="lower-limb-options-container">{renderOptions()}</div>
    </div>
  );
};

export default LowerLimbFunctionalIndex;
