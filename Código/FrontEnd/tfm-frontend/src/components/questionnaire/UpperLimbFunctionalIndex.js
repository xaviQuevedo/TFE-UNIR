import React from "react";
import "../../styles/UpperLimbFunctionalIndex.css";

const UpperLimbFunctionalIndex = ({ question, selectedValue, onSelect }) => {
  const renderOptions = () => {
    return Object.entries(question.options).map(([key, value]) => (
      <button
        key={key}
        className={`upper-limb-option ${
          selectedValue === key ? "selected" : ""
        }`}
        onClick={() => onSelect(key)}
      >
        <span className="upper-limb-option-score">{key}</span>
        <span className="upper-limb-option-text">{value}</span>
      </button>
    ));
  };

  return (
    <div className="upper-limb-question-container">
      <div className="upper-limb-options-container">{renderOptions()}</div>
    </div>
  );
};

export default UpperLimbFunctionalIndex;
