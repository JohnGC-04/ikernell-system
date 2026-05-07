import { writable } from 'svelte/store';

// Creamos un store que inicia buscando el token en el navegador
const tokenPersistido = localStorage.getItem('token');

export const auth = writable({
    isLoggedIn: !!tokenPersistido,
    token: tokenPersistido || null,
    userEmail: null,
    rol: null
});

// Función para guardar el login
export const login = (token, email, rol) => {
    localStorage.setItem('token', token);
    auth.set({ isLoggedIn: true, token, userEmail: email, rol });
};

// Función para cerrar sesión
export const logout = () => {
    localStorage.removeItem('token');
    auth.set({ isLoggedIn: false, token: null, userEmail: null, rol: null });
};