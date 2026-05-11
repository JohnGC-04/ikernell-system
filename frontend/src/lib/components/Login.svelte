<script>
  import axios from "axios";

  let email = "";
  let password = "";
  let loading = false;
  let errorMessage = "";

  async function handleLogin() {
    loading = true;
    errorMessage = "";
    try {
      const response = await axios.post(
        "http://localhost:8080/api/auth/login",
        {
          email,
          password,
        },
      );

      const token = response.data.token; // Extraer del objeto Map que envía Java
      localStorage.setItem("token", token); // Guardar el token en localStorage (sesión)
      console.log("¡Sesión iniciada!");

      alert("Acceso concedido. ¡Bienvenido a ikernell!");
      // Aquí redirigiremos al Dashboard luego
    } catch (error) {
      errorMessage = "Credenciales incorrectas o servidor no disponible.";
    } finally {
      loading = false;
    }
  }
</script>

<div class="min-h-screen flex items-center justify-center bg-black-100 px-4">
  <div class="max-w-md w-full bg-white rounded-xl shadow-lg p-8">
    <div class="text-center mb-10">
      <h1 class="text-3xl font-bold text-indigo-600">ikernell-system</h1>
      <p class="text-gray-500">Gestión de Proyectos de Ingeniería</p>
    </div>

    <form on:submit|preventDefault={handleLogin} class="space-y-6">
      <div>
        <label for="email" class="block text-sm font-medium text-gray-700">
          Email Corporativo
        </label>
        <input
          id="email"
          type="email"
          bind:value={email}
          class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-900 focus:ring-indigo-500 focus:border-indigo-500"
        />
      </div>

      <div>
        <label for="password" class="block text-sm font-medium text-gray-700">
          Contraseña
        </label>
        <input
          id="password"
          bind:value={password}
          type="password"
          required
          class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-900 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
        />
      </div>

      {#if errorMessage}
        <p class="text-red-500 text-sm text-center">{errorMessage}</p>
      {/if}

      <button
        type="submit"
        disabled={loading}
        class="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:bg-indigo-300"
      >
        {loading ? "Verificando..." : "Iniciar Sesión"}
      </button>
    </form>
  </div>
</div>
