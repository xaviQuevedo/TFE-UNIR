import React from "react";
import "../../styles/McGillPainQuestionnaire.css";

const McGillPainQuestionnaire = ({ question, selectedValue, onSelect }) => {
  const renderOptions = () => {
    if (Array.isArray(question.options)) {
      return question.options.map((option) => (
        <button
          key={option}
          className={`mcgill-option ${
            selectedValue?.includes(option) ? "selected" : ""
          }`}
          onClick={() =>
            onSelect(
              selectedValue?.includes(option)
                ? selectedValue.filter((val) => val !== option)
                : [...(selectedValue || []), option]
            )
          }
        >
          {option}
        </button>
      ));
    } else if (typeof question.options === "object") {
      return Object.entries(question.options).map(([key, value]) => (
        <button
          key={key}
          className={`mcgill-scale-option ${
            selectedValue === key ? "selected" : ""
          }`}
          onClick={() => onSelect(key)}
        >
          {value}
        </button>
      ));
    }
    return null;
  };

  return (
    <div className="mcgill-question-container">
      <div className="mcgill-options-container">{renderOptions()}</div>
    </div>
  );
};

export default McGillPainQuestionnaire;
