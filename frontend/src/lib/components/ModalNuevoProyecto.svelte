<script lang="ts">
  import { createEventDispatcher } from 'svelte';
  import api from '/home/fabrica/Desktop/ikernell/ikernell-system/frontend/src/lib/api';

  export let show = false; // Controla si se ve o no
  const dispatch = createEventDispatcher();

  let nuevoProyecto = {
    nombre: '',
    descripcion: '',
    presupuesto: 0,
    estado: 'PLANIFICACION',
    id_lider: 1 // Por ahora quemado, luego lo sacas del usuario logueado
  };

  async function guardar() {
    try {
      await api.post('/proyectos', nuevoProyecto);
      dispatch('proyectoCreado'); // Avisamos al Dashboard para que recargue la tabla
      show = false;
    } catch (err) {
      alert("Error al crear el proyecto. Revisa tus permisos (¿Eres COORDINADOR?)");
    }
  }
</script>

{#if show}
  <div class="fixed inset-0 bg-black/70 backdrop-blur-sm flex items-center justify-center z-50 p-4">
    <div class="bg-gray-800 border border-gray-700 w-full max-w-md rounded-2xl shadow-2xl p-6">
      <h3 class="text-xl font-bold text-white mb-6">Registrar Nuevo Proyecto</h3>
      
      <div class="space-y-4">
        <div>
          <label class="block text-xs font-semibold text-gray-400 uppercase mb-1">Nombre del Proyecto</label>
          <input bind:value={nuevoProyecto.nombre} type="text" class="w-full bg-gray-900 border border-gray-700 rounded-lg p-2 text-white focus:ring-2 focus:ring-indigo-500 outline-none" />
        </div>

        <div>
          <label class="block text-xs font-semibold text-gray-400 uppercase mb-1">Descripción</label>
          <textarea bind:value={nuevoProyecto.descripcion} class="w-full bg-gray-900 border border-gray-700 rounded-lg p-2 text-white h-24 focus:ring-2 focus:ring-indigo-500 outline-none"></textarea>
        </div>

        <div class="grid grid-cols-2 gap-4">
          <div>
            <label class="block text-xs font-semibold text-gray-400 uppercase mb-1">Presupuesto ($)</label>
            <input bind:value={nuevoProyecto.presupuesto} type="number" class="w-full bg-gray-900 border border-gray-700 rounded-lg p-2 text-white focus:ring-2 focus:ring-indigo-500 outline-none" />
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-400 uppercase mb-1">Estado</label>
            <select bind:value={nuevoProyecto.estado} class="w-full bg-gray-900 border border-gray-700 rounded-lg p-2 text-white focus:ring-2 focus:ring-indigo-500 outline-none">
              <option value="PLANIFICACION">Planificación</option>
              <option value="ACTIVO">Activo</option>
            </select>
          </div>
        </div>
      </div>

      <div class="flex space-x-3 mt-8">
        <button on:click={() => show = false} class="flex-1 bg-gray-700 hover:bg-gray-600 text-white font-bold py-2 rounded-lg transition">Cancelar</button>
        <button on:click={guardar} class="flex-1 bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-2 rounded-lg transition shadow-lg shadow-indigo-500/20">Guardar Proyecto</button>
      </div>
    </div>
  </div>
{/if}