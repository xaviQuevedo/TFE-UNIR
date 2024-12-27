import React, { useState } from "react";
import "../../styles/questionnaires/ProgressBarScale.css";

const ProgressBarScale = ({ options, selectedValue, onSelect }) => {
  const [hoverValue, setHoverValue] = useState(null);

  const handleClick = (value) => {
    onSelect(value);
  };

  const handleMouseEnter = (value) => {
    setHoverValue(value);
  };

  const handleMouseLeave = () => {
    setHoverValue(null);
  };

  return (
    <div className="progress-bar-container">
      <div className="progress-bar">
        {Array.from({ length: 10 }, (_, index) => index.toString()).map(
          (value) => (
            <div
              key={value}
              className={`progress-step ${
                selectedValue === value ? "selected" : ""
              } ${hoverValue === value ? "hovered" : ""}`}
              onClick={() => handleClick(value)}
              onMouseEnter={() => handleMouseEnter(value)}
              onMouseLeave={handleMouseLeave}
            >
              <span className="progress-label">
                {hoverValue === value || selectedValue === value ? value : ""}
              </span>
            </div>
          )
        )}
      </div>
      <div className="progress-labels">
        <span>{options["0"]}</span>
        <span>{options["10"]}</span>
      </div>
    </div>
  );
};

export default ProgressBarScale;
