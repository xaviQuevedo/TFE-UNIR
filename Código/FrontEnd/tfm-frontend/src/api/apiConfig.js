import axios from "axios";

const BASE_URLS = {
  gestionUsuarios: "http://localhost:8080",
  gestionFisioterapeutas: "http://localhost:8081",
};

const createApiInstance = (service) => {
  if (!BASE_URLS[service]) {
    throw new Error(`Service ${service} is not supported`);
  }
  return axios.create({
    baseURL: BASE_URLS[service],
    headers: {
      "Content-Type": "application/json",
    },
  });
};

export default createApiInstance;
