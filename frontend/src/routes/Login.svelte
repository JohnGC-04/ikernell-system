<script>
  import axios from "axios";
  import { push } from "svelte-spa-router";

  let email = ""; // Para almacenar las credenciales del usuario
  let password = ""; // Para almacenar las credenciales del usuario
  let loading = false; // Para controlar el estado de carga
  let errorMessage = ""; // Para mostrar errores de autenticación
  let showPassword = false; // Controla si se ve la clave
  let rememberMe = false; // Para el futuro "Recuérdame"

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

      push("/dashboard");
    } catch (error) {
      errorMessage = "Credenciales Invalidas, Por Favor Intente de Nuevo.";
    } finally {
      loading = false;
    }
  }
</script>

<div class="min-h-screen flex items-center justify-center bg-blue px-4">
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

      <div class="flex items-center justify-between mt-2">
        <div class="flex items-center">
          <input
            id="show-password"
            type="checkbox"
            bind:checked={showPassword}
            class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
          />
          <label for="show-password" class="ml-2 block text-sm text-gray-700">
            Mostrar contraseña
          </label>
        </div>

        <div class="text-sm">
          <a
            href="#/recuperar"
            class="font-medium text-indigo-600 hover:text-indigo-500"
          >
            ¿Olvidaste tu contraseña?
          </a>
        </div>
      </div>

      <div class="flex items-center mt-4">
        <input
          id="remember-me"
          type="checkbox"
          bind:checked={rememberMe}
          class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
        />
        <label for="remember-me" class="ml-2 block text-sm text-gray-700">
          Recuérdame
        </label>
      </div>

      <input
        id="password"
        bind:value={password}
        type={showPassword ? "text" : "password"}
        required
        class="..."
      />

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
