<script lang="ts"> // Asegúrate de tener lang="ts"
  import { onMount } from 'svelte';
  import axios from 'axios';

  // 1. Definimos la estructura del proyecto según tu DTO de Java
  interface Proyecto {
    id: number;
    nombre: string;
    descripcion: string;
    // Añade aquí los demás campos que tenga tu ProyectoDTO
  }

  // 2. Inicializamos con el tipo correcto
  let proyectos: Proyecto[] = [];
  let loading: boolean = true;
  let error: string = "";

  onMount(async () => {
    const token = localStorage.getItem('token');
    
    try {
      // Especificamos que la respuesta de axios trae un array de Proyectos
      const response = await axios.get<Proyecto[]>("http://localhost:8080/api/proyectos", {
        headers: { Authorization: `Bearer ${token}` }
      });
      proyectos = response.data;
    } catch (err) {
      error = "Error al cargar proyectos.";
    } finally {
      loading = false;
    }
  });
</script>

<div class="min-h-screen bg-gray-900 text-white p-8">
  <header class="flex justify-between items-center mb-8">
    <h1 class="text-3xl font-bold text-indigo-400">Panel de Proyectos</h1>
    <span class="bg-indigo-900 text-indigo-200 px-3 py-1 rounded-full text-xs">Conectado</span>
  </header>

  {#if loading}
    <p>Cargando datos de ingeniería...</p>
  {:else if error}
    <p class="text-red-400">{error}</p>
  {:else}
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      {#each proyectos as proyecto}
        <div class="bg-gray-800 border border-gray-700 p-6 rounded-xl hover:border-indigo-500 transition-colors">
          <h3 class="text-xl font-bold text-white mb-2">{proyecto.nombre}</h3>
          <p class="text-gray-400 text-sm mb-4 line-clamp-2">{proyecto.descripcion}</p>
          <div class="flex justify-between items-center">
            <span class="text-xs font-mono text-indigo-300">ID: {proyecto.id}</span>
            <button class="text-indigo-400 hover:text-indigo-300 text-sm font-medium">Ver detalles →</button>
          </div>
        </div>
      {/each}
    </div>
  {/if}
</div>