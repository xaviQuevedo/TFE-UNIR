import { useState, useEffect, useCallback } from "react";

import { userService } from "../api/apiConfig";

const useUserManagement = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);


  const fetchUsers = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const token = localStorage.getItem("token");

      const response = await userService.get("/users/admin/all", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setUsers(response.data);
    } catch (error) {
      setError(error);
    }
    setLoading(false);
  }, []);

  const deleteUser = async (id) => {
    try {
      const token = localStorage.getItem("token");
      await userService.delete(`/users/${id}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setUsers((prevUsers) => prevUsers.filter((user) => user.id !== id));
      fetchUsers();
    } catch (error) {
      setError(error);
    }
  };

  const updateUser = async (id, updatedData) => {
    try {
      const token = localStorage.getItem("token");
      await userService.put(`/users/${id}`, updatedData, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setUsers((prevUsers) =>
        prevUsers.map((user) =>
          user.user_id === id ? { ...user, ...updatedData } : user
        )
      );
    } catch (error) {
      setError(error);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, [fetchUsers]);

  return { users, loading, error, deleteUser, updateUser };
};

export default useUserManagement;
