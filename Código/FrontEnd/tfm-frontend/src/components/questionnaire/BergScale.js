import React from "react";
import "../../styles/questionnaires/BergScale.css";

const BergScale = ({ options, selectedValue, onSelect }) => {
  return (
    <div className="berg-scale-container">
      {options.map((option, index) => (
        <button
          key={index}
          className={`berg-scale-option ${
            selectedValue === option ? "selected" : ""
          }`}
          onClick={() => onSelect(option)}
        >
          {option}
        </button>
      ))}
    </div>
  );
};

export default BergScale;
