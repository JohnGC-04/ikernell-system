<script lang="ts">
  import { push } from "svelte-spa-router";

  // Función para destruir la sesión
  function handleLogout() {
    // Borramos de ambos por seguridad
    localStorage.removeItem("token");
    sessionStorage.removeItem("token");
    
    // Redirigir al Login
    push("/");
  }
</script>

<nav class="bg-gray-800 border-b border-gray-700 px-6 py-3">
  <div class="flex justify-between items-center max-w-7xl mx-auto">
    <div class="flex items-center space-x-4">
      <div class="bg-indigo-600 p-2 rounded-lg">
        <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
        </svg>
      </div>
      <span class="text-xl font-bold text-white tracking-tight">ikernell<span class="text-indigo-400">-system</span></span>
    </div>

    <div class="hidden md:flex space-x-8">
      <a href="#/dashboard" class="text-indigo-400 font-medium border-b-2 border-indigo-400 pb-1">Proyectos</a>
      <a href="#/actividades" class="text-gray-400 hover:text-white transition-colors">Actividades</a>
      <a href="#/usuarios" class="text-gray-400 hover:text-white transition-colors">Usuarios</a>
    </div>

    <div class="flex items-center space-x-4">
      <div class="text-right hidden sm:block">
        <p class="text-xs text-gray-400">Sesión iniciada</p>
        <p class="text-sm font-semibold text-gray-200">Ingeniero de Proyectos</p>
      </div>
      <button 
        on:click={handleLogout}
        class="bg-gray-700 hover:bg-red-600 text-gray-200 hover:text-white px-4 py-2 rounded-md text-sm font-bold transition-all duration-300 flex items-center space-x-2"
      >
        <span>Salir</span>
        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
        </svg>
      </button>
    </div>
  </div>
</nav>