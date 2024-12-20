import React from "react";
import "../../styles/BarthelIndex.css";

const BarthelIndex = ({ question, selectedValue, onSelect }) => {
  const renderOptions = () => {
    return Object.entries(question.options).map(([key, value]) => (
      <button
        key={key}
        className={`barthel-option ${selectedValue === key ? "selected" : ""}`}
        onClick={() => onSelect(key)}
      >
        <span className="barthel-option-score">{key}</span>
        <span className="barthel-option-text">{value}</span>
      </button>
    ));
  };

  return (
    <div className="barthel-question-container">
      <div className="barthel-options-container">{renderOptions()}</div>
    </div>
  );
};

export default BarthelIndex;
