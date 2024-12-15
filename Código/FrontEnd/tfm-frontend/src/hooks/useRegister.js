import { useState } from "react";
import { userService } from "../api/apiConfig";

const useRegister = () => {
  const [status, setStatus] = useState("");
  const [loading, setLoading] = useState(false);

  const register = async ({ email, password, role }) => {
    setLoading(true);
    setStatus("");
    try {
      const token = localStorage.getItem("token");

      await userService.post(
        "/users/admin/register",
        { email, password, role },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      setStatus("User registered");
    } catch (error) {
      setStatus("Error registering user");
    }
    setLoading(false);
  };

  return { register, status, loading };
};

export default useRegister;
