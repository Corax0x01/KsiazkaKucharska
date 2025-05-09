import {defineStore} from "pinia";
import {ref} from "vue";

export const useAuthStore = defineStore('jwtToken', () => {
    const token = ref(null);
    const authenticated = ref(false);

    if (localStorage.getItem("authenticated")) {
        authenticated.value = localStorage.getItem("authenticated") === "true";
        token.value = localStorage.getItem("token");
    }

    const setToken = (newToken) => {
        token.value = newToken;
    }

    const getToken = () => {
        return token.value;
    }

    const setAuthenticated = (isAuthenticated) => {
        authenticated.value = isAuthenticated;
    }

    const getAuthenticated = () => {
        return authenticated.value;
    }

    const logout = () => {
        token.value = null;
        authenticated.value = false;
        localStorage.removeItem("token");
        localStorage.removeItem("authenticated");
    }

    return {
        token,
        setToken,
        getToken,
        authenticated,
        setAuthenticated,
        getAuthenticated,
        logout
    };
});