import { writable } from 'svelte/store';

// Creamos un store que guardará el token
export const token = writable(localStorage.getItem('token') || null);

// Función para cerrar sesión
export const logout = () => {
    localStorage.removeItem('token');
    token.set(null);
    window.location.href = '/'; // Redirigir al login
};