import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { userService } from "../api/apiConfig";

const useRegister = () => {
  const [status, setStatus] = useState("");
  const [loading, setLoading] = useState(false);
  const [showConfirmation, setShowConfirmation] = useState(false); 
  const navigate = useNavigate();

  const register = async ({ email, password, role, name, last_name }) => {
    setLoading(true);
    setStatus("");
    try {
      const token = localStorage.getItem("token");

      await userService.post(
        "/users",
        { email, password, role, name, last_name },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      setStatus("Usuario registrado exitosamente.");
      setShowConfirmation(true);
    } catch (error) {
      setStatus("Error al registrar usuario.");
    }
    setLoading(false);
  };

  const handleRedirect = () => {
    setShowConfirmation(false);
    navigate("/admin/dashboard");
  };

  return { register, status, loading, showConfirmation, handleRedirect };
};

export default useRegister;
