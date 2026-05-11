<script lang="ts">
  import Router from "svelte-spa-router";
  import Login from "./routes/Login.svelte";
  import Dashboard from "./routes/Dashboard.svelte";
  import Navbar from "./lib/components/Navbar.svelte";
  import api from "./lib/api";
  import { onMount } from "svelte";
  import axios from "axios"; // Importamos axios para el tipado del error
  import { push } from "svelte-spa-router";
  import { writable } from "svelte/store";
  import { get } from "svelte/store";
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