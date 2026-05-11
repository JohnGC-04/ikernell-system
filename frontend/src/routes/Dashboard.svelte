<script>
  import { onMount } from 'svelte';
  import { logout } from '../lib/auth';

  let proyectos = [];
  let loading = true;

  onMount(async () => {
    const savedToken = localStorage.getItem('token');
    
    if (!savedToken) {
      window.location.href = '/'; // Si no hay token, fuera
      return;
    }

    try {
      // Aquí llamarás a tu API de Spring Boot
      // const res = await fetch('http://localhost:8080/api/proyectos', {
      //   headers: { 'Authorization': `Bearer ${savedToken}` }
      // });
      // proyectos = await res.json();
    } catch (err) {
      console.error("Error cargando proyectos", err);
    } finally {
      loading = false;
    }
  });
</script>

<div class="min-h-screen bg-gray-900 text-white p-8">
  <nav class="flex justify-between items-center mb-10 border-b border-gray-700 pb-4">
    <h1 class="text-2xl font-bold text-indigo-400">ikernell-system</h1>
    <button 
      on:click={logout}
      class="bg-red-600 hover:bg-red-700 px-4 py-2 rounded-lg text-sm transition"
    >
      Cerrar Sesión
    </button>
  </nav>

  <main>
    <h2 class="text-3xl font-semibold mb-6">Panel de Proyectos</h2>
    
    {#if loading}
      <p class="text-gray-400">Cargando proyectos de ingeniería...</p>
    {:else}
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div class="bg-gray-800 p-6 rounded-xl border border-gray-700">
          <h3 class="text-xl font-bold mb-2">Proyecto Alpha</h3>
          <p class="text-gray-400 text-sm">Estado: En progreso</p>
        </div>
      </div>
    {/if}
  </main>
</div>