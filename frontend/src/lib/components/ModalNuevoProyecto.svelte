<script lang="ts">
  import api from "/home/fabrica/Desktop/ikernell/ikernell-system/frontend/src/lib/api";
  let { show = $bindable(false), onProyectoCreado } = $props();

  let nuevoProyecto = {
    nombre: "",
    descripcion: "",
    presupuesto: 0.1,
    // ESTE CAMPO ES EL QUE DA EL ERROR
    estado: "Pendiente", // Debe ser "Pendiente", "En Progreso" o "Completado"
    idLider: 1,
  };

  async function guardar() {
    try {
      await api.post("/proyectos", nuevoProyecto);
      if (onProyectoCreado) onProyectoCreado(); // Llama al callback para recargar la lista
      show = false;
    } catch (err) {
      alert(
        "Error al crear el proyecto. Revisa tus permisos (¿Eres COORDINADOR?)",
      );
    }
  }
</script>

{#if show}
  <div
    class="fixed inset-0 bg-black/70 backdrop-blur-sm flex items-center justify-center z-50 p-4"
  >
    <div
      class="bg-gray-800 border border-gray-700 w-full max-w-md rounded-2xl shadow-2xl p-6"
    >
      <h3 class="text-xl font-bold text-white mb-6">
        Registrar Nuevo Proyecto
      </h3>

      <div class="space-y-4">
        <div>
          <!-- Añadimos 'for' y un 'id' al input para arreglar el error de accesibilidad -->
          <label
            for="nombre"
            class="block text-xs font-semibold text-gray-400 uppercase mb-1"
            >Nombre del Proyecto</label
          >
          <input
            id="nombre"
            bind:value={nuevoProyecto.nombre}
            type="text"
            class="w-full bg-gray-900 border border-gray-700 rounded-lg p-2 text-white focus:ring-2 focus:ring-indigo-500 outline-none"
          />
        </div>

        <div>
          <label
            for="desc"
            class="block text-xs font-semibold text-gray-400 uppercase mb-1"
            >Descripción</label
          >
          <textarea
            id="desc"
            bind:value={nuevoProyecto.descripcion}
            class="w-full bg-gray-900 border border-gray-700 rounded-lg p-2 text-white h-24 focus:ring-2 focus:ring-indigo-500 outline-none"
          ></textarea>
        </div>

        <div class="grid grid-cols-2 gap-4">
          <div>
            <label
              for="presupuesto"
              class="block text-xs font-semibold text-gray-400 uppercase mb-1"
              >Presupuesto ($)</label
            >
            <input
              id="presupuesto"
              bind:value={nuevoProyecto.presupuesto}
              type="number"
              class="w-full bg-gray-900 border border-gray-700 rounded-lg p-2 text-white focus:ring-2 focus:ring-indigo-500 outline-none"
            />
          </div>
          <div>
            <label
              for="estado"
              class="block text-xs font-semibold text-gray-400 uppercase mb-1"
              >Estado</label
            >
            <select
              bind:value={nuevoProyecto.estado}
              class="bg-gray-900 border border-gray-700 rounded-lg p-2 text-gray-200
         focus:ring-indigo-500 focus:border-indigo-500 block w-full
         appearance-none cursor-pointer hover:bg-gray-700 transition-colors"
            >
              <option value="Pendiente" class="bg-gray-800">Pendiente</option>
              <option value="En Progreso" class="bg-gray-800"
                >En Progreso</option
              >
              <option value="Completado" class="bg-gray-800">Completado</option>
            </select>
          </div>
        </div>
      </div>

      <div class="flex space-x-3 mt-8">
        <button
          onclick={() => (show = false)}
          class="flex-1 bg-gray-700 hover:bg-gray-600 text-white font-bold py-2 rounded-lg transition"
          >Cancelar</button
        >
        <button
          onclick={guardar}
          class="flex-1 bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-2 rounded-lg transition shadow-lg shadow-indigo-500/20"
          >Guardar Proyecto</button
        >
      </div>
    </div>
  </div>
{/if}
