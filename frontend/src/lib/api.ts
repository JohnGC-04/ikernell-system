import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api'
});

api.interceptors.request.use((config) => {
    // BUSCAMOS EN AMBOS: Primero localStorage, si no, sessionStorage
    const token = (localStorage.getItem("token") || sessionStorage.getItem("token"))?.replace(/"/g, '');

    console.log("Interceptor ejecutado. Token encontrado:", token ? "SÍ" : "NO");

    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
        console.log("Header Authorization enviado.");
    }

    return config;
}, (error) => {
    return Promise.reject(error);
});

export default api;