import axios from "axios";

const BASE_URLS = {
  gestionUsuarios: "http://localhost:8762/ms-gestion-usuarios",
  gestionFisioterapeutas: "http://localhost:8762/ms-gestion-fisioterapeutas",
  gestionCuestionarios: "http://localhost:8762/ms-gestion-cuestionarios",
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

export const userService = createApiInstance("gestionUsuarios");
export const physiotherapistService = createApiInstance("gestionFisioterapeutas");
export const questionnaireService = createApiInstance("gestionCuestionarios");
export default createApiInstance;
