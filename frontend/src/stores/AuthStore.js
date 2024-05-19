import {defineStore} from "pinia";
import {ref} from "vue";

export const useAuthStore = defineStore('jwtToken', () => {
    const token = ref(null);
    const authenticated = ref(false);

    return {token, authenticated};
});