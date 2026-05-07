<script>
    import axios from 'axios';
    import { login } from '../stores/authStore';

    let email = '';
    let password = '';
    let error = '';

    async function handleLogin() {
        try {
            const response = await axios.post('http://localhost:8080/api/auth/login', {
                email,
                password
            });
            
            // Aquí llamaríamos a una función para decodificar el JWT y sacar el ROL
            login(response.data.token, email, "LIDER"); 
            alert("¡Bienvenido al sistema ikernell!");
        } catch (err) {
            error = "Credenciales incorrectas o servidor caído";
        }
    }
</script>

<div class="login-container">
    <h2>ikernell-system</h2>
    <input type="email" bind:value={email} placeholder="Email" />
    <input type="password" bind:value={password} placeholder="Password" />
    <button on:click={handleLogin}>Entrar</button>
    {#if error} <p style="color: red;">{error}</p> {/if}
</div>