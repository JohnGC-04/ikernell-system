<script lang="ts">
  import axios from "axios";
  import { push } from "svelte-spa-router";

  let email = "";
  let password = "";
  let loading = false;
  let errorMessage = "";
  let showPassword = false;
  let rememberMe = false;

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

      const token = response.data.token;

      // Lógica de "Recuérdame"
      if (rememberMe) {
        localStorage.setItem("token", token);
      } else {
        sessionStorage.setItem("token", token);
      }
      setTimeout(() => {
        push("/dashboard");
      }, 100); // Pequeña demora para mostrar el mensaje de éxito

      push("/dashboard");
    } catch (error) {
      errorMessage = "Credenciales inválidas. Por favor, inténtelo de nuevo.";
    } finally {
      loading = false;
    }
  }
</script>

<div class="min-h-screen flex items-center justify-center bg-gray-900 px-4">
  <div class="max-w-md w-full bg-white rounded-xl shadow-lg p-8">
    <div class="text-center mb-10">
      <h1 class="text-3xl font-bold text-indigo-600">ikernell-system</h1>
      <p class="text-gray-500 font-medium">
        Gestión de Proyectos de Ingeniería
      </p>
    </div>

    <form on:submit|preventDefault={handleLogin} class="space-y-4">
      <div>
        <label for="email" class="block text-sm font-medium text-gray-700"
          >Email Corporativo</label
        >
        <input
          id="email"
          type="email"
          bind:value={email}
          required
          class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md text-gray-900 focus:ring-indigo-500 focus:border-indigo-500"
        />
      </div>

      <div>
        <label for="password" class="block text-sm font-medium text-gray-700"
          >Contraseña</label
        >
        <input
          id="password"
          bind:value={password}
          type={showPassword ? "text" : "password"}
          required
          class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md text-gray-900 focus:ring-indigo-500 focus:border-indigo-500"
        />
      </div>

      <div class="flex items-center justify-between">
        <div class="flex items-center">
          <input
            id="show-pass"
            type="checkbox"
            bind:checked={showPassword}
            class="h-4 w-4 text-indigo-600 rounded"
          />
          <label for="show-pass" class="ml-2 text-xs text-gray-600"
            >Mostrar</label
          >
        </div>
        <a
          href="#/recuperar"
          class="text-xs font-semibold text-indigo-600 hover:text-indigo-500"
          >¿Olvidaste tu clave?</a
        >
      </div>

      <div class="flex items-center">
        <input
          id="remember"
          type="checkbox"
          bind:checked={rememberMe}
          class="h-4 w-4 text-indigo-600 rounded"
        />
        <label for="remember" class="ml-2 text-xs text-gray-600"
          >Recordar sesión</label
        >
      </div>

      {#if errorMessage}
        <p class="text-red-500 text-xs text-center">{errorMessage}</p>
      {/if}

      <button
        type="submit"
        disabled={loading}
        class="w-full py-2 px-4 rounded-md text-white bg-indigo-600 hover:bg-indigo-700 font-bold disabled:bg-indigo-300 transition-colors"
      >
        {loading ? "Verificando..." : "Entrar al Sistema"}
      </button>
    </form>
  </div>
</div>
