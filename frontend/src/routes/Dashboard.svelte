<script lang="ts">
  import { onMount } from "svelte";
  import { push } from "svelte-spa-router";
  import api from "../lib/api";
  import Navbar from "../lib/components/Navbar.svelte";
  import axios from "axios"; // Importamos axios para el tipado del error
  import ModalNuevoProyecto from "../lib/components/ModalNuevoProyecto.svelte";

  interface Proyecto {
    id: number;
    nombre: string;
    presupuesto: number;
    estado: string;
    id_lider: number;
  }

  let proyectos: Proyecto[] = [];
  let loading = true;
  let modalShow = false;
  let mostrarModal = false;

  async function cargarProyectos() {
    loading = true;
    try {
      const res = await api.get("/proyectos");
      proyectos = res.data;
    } catch (err) {
      // ... tu lógica de error
    } finally {
      loading = false;
    }
  }

  onMount(async () => {
    const token =
      localStorage.getItem("token") || sessionStorage.getItem("token");

    if (!token) {
      console.log("no hay token, redirigiendo");
      console.warn(
        "No se encontró token de autenticación. Redirigiendo al login.",
      );
      push("/");
      return;
    }

    try {
    const res = await api.get('/proyectos');
    proyectos = res.data;
} catch (err) {
    // ESTO ES CLAVE:
    if (axios.isAxiosError(err)) {
        console.error("Status:", err.response?.status);
        console.error("Data del error:", err.response?.data);
        
        // Comenta el push para que no te saque de la página y puedas leer el log
        // push("/"); 
    }
}
  });
</script>

<div class="min-h-screen bg-gray-900 flex flex-col">
  <Navbar />

  <main class="max-w-7xl mx-auto w-full p-6 space-y-6">
    <div
      class="flex justify-between items-center border-b border-gray-800 pb-4"
    >
      <h2
        class="text-2xl font-bold text-white border-l-4 border-indigo-500 pl-4"
      >
        Gestión de Proyectos
      </h2>
      <button
        on:click={() => (mostrarModal = true)}
        class="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-lg text-sm font-bold transition shadow-lg"
      >
        + Nuevo Proyecto
      </button>
    </div>

    <div class="space-y-4">
      <h3 class="text-gray-400 text-sm font-medium uppercase tracking-wider">
        Listado Maestro de Proyectos
      </h3>

      <div
        class="overflow-hidden rounded-xl border border-gray-700 bg-gray-800 shadow-2xl"
      >
        <table class="min-w-full divide-y divide-gray-700">
          <thead class="bg-gray-700/50">
            <tr>
              <th
                class="px-6 py-3 text-left text-xs font-medium text-gray-400 uppercase tracking-wider"
                >ID</th
              >
              <th
                class="px-6 py-3 text-left text-xs font-medium text-gray-400 uppercase tracking-wider"
                >Proyecto</th
              >
              <th
                class="px-6 py-3 text-left text-xs font-medium text-gray-400 uppercase tracking-wider"
                >Presupuesto</th
              >
              <th
                class="px-6 py-3 text-left text-xs font-medium text-gray-400 uppercase tracking-wider"
                >Estado</th
              >
              <th
                class="px-6 py-3 text-right text-xs font-medium text-gray-400 uppercase tracking-wider"
                >Acciones</th
              >
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-700">
            {#each proyectos as p}
              <tr class="hover:bg-gray-700/30 transition-colors">
                <td
                  class="px-6 py-4 whitespace-nowrap text-sm text-indigo-400 font-mono"
                  >#{p.id}</td
                >
                <td
                  class="px-6 py-4 whitespace-nowrap text-sm font-semibold text-gray-200"
                  >{p.nombre}</td
                >
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-300">
                  {p.presupuesto
                    ? `$${p.presupuesto.toLocaleString("es-CO")}`
                    : "No asignado"}
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  {#if p.estado === "ACTIVO"}
                    <span
                      class="px-2 py-1 text-xs font-bold rounded-full bg-green-900 text-green-200 ring-1 ring-green-500/50"
                    >
                      ● ACTIVO
                    </span>
                  {:else if p.estado === "PLANIFICACION"}
                    <span
                      class="px-2 py-1 text-xs font-bold rounded-full bg-amber-900 text-amber-200 ring-1 ring-amber-500/50"
                    >
                      ● PLANIFICACIÓN
                    </span>
                  {:else}
                    <span
                      class="px-2 py-1 text-xs font-bold rounded-full bg-gray-700 text-gray-300"
                    >
                      {p.estado}
                    </span>
                  {/if}
                </td>
                <td
                  class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium"
                >
                  <button
                    class="text-indigo-400 hover:text-indigo-300 mr-3 underline decoration-transparent hover:decoration-indigo-300"
                    >Editar</button
                  >
                  <button
                    class="text-gray-500 hover:text-red-400 transition-colors"
                    >Ver Balance</button
                  >
                </td>
              </tr>
            {/each}
          </tbody>
        </table>

        {#if proyectos.length === 0 && !loading}
          <div class="text-center py-12">
            <p class="text-gray-500">No hay proyectos registrados.</p>
          </div>
        {/if}
      </div>
    </div>
  </main>
  <ModalNuevoProyecto
    bind:show={mostrarModal}
    on:proyectoCreado={() => cargarProyectos()}
  />
</div>
