import { useState } from "react";
import { userService } from "../api/apiConfig";

const useLogin = () => {
  const [status, setStatus] = useState("");
  const [loading, setLoading] = useState(false);

  const login = async ({ email, password }) => {
    setLoading(true);
    setStatus("");
    try {
      const response = await userService.post("/users/login", {
        email,
        password,
      });
      const { token, role } = response.data;
      localStorage.setItem("token", token);
      localStorage.setItem("role", role);
      setStatus("Logged in");
      return role;
    } catch (error) {
      setStatus("Error al iniciar sesión");
    }
    setLoading(false);
  };

  return { login, status, loading };
};

export default useLogin;
