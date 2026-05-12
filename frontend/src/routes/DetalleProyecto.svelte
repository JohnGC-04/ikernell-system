<script lang="ts">
  import { onMount } from 'svelte';
  import api from '../lib/api';
  import Navbar from '../lib/components/Navbar.svelte';

  export let params = { id: '' }; // svelte-spa-router inyecta esto automáticamente
  let proyecto: any = null;
  let loading = true;

  onMount(async () => {
    try {
      const res = await api.get(`/proyectos/${params.id}`);
      proyecto = res.data;
    } catch (error) {
      console.error("Error cargando detalle:", error);
    } finally {
      loading = false;
    }
  });
</script>

<Navbar />
{#if loading}
  <p class="text-white p-10">Cargando detalles...</p>
{:else if proyecto}
  <main class="max-w-7xl mx-auto p-6">
    <h1 class="text-3xl font-bold text-white">{proyecto.nombre}</h1>
    <p class="text-gray-400 mt-2">{proyecto.descripcion}</p>
    <!-- Aquí irán las Etapas y Actividades después -->
  </main>
{/if}