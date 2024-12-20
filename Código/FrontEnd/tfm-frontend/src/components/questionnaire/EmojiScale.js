import React from "react";
import "../../styles/EmojiScale.css";

const EmojiScale = ({ options, selectedValue, onSelect }) => {
  const renderEmoji = (key) => {
    switch (key) {
      case "0":
        return "😊";
      case "2":
        return "🙂";
      case "4":
        return "😐";
      case "6":
        return "🙁";
      case "8":
        return "😟";
      case "10":
        return "😭";
      default:
        return "❓";
    }
  };

  return (
    <div className="emoji-scale-container">
      {Object.entries(options).map(([key, value]) => (
        <div
          key={key}
          className={`emoji-option ${selectedValue === key ? "selected" : ""}`}
          onClick={() => onSelect(key)}
        >
          <span className="emoji">{renderEmoji(key)}</span>
          <p className="emoji-text">{value}</p>
        </div>
      ))}
    </div>
  );
};

export default EmojiScale;
