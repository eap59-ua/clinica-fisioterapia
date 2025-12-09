import api from "./api";

export default {
  async getServicios() {
    const response = await api.get("/public/servicios");
    return response.data;
  },

  async getFisioterapeutas() {
    const response = await api.get("/public/fisioterapeutas");
    return response.data;
  },
};
