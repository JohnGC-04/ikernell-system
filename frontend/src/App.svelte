<script lang="ts">
  import Router from "svelte-spa-router";
  import Login from "./routes/Login.svelte";
  import Dashboard from "./routes/Dashboard.svelte";
  import "./app.css";

  // Definimos el mapa de rutas: clave es la URL, valor es el componente
  const routes = {
    "/": Login,
    "/dashboard": Dashboard,
    // Puedes añadir más rutas aquí: "/proyectos": Proyectos
  };
</script>

<main>
  <Router {routes} />
</main>