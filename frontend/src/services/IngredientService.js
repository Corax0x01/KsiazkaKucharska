const API_URL = import.meta.env.VITE_API_URL;
const API_PORT = import.meta.env.VITE_API_PORT;

const IngredientService = {
    getAllIngredients: async function (jwtToken){
        try {
            const headers = {
                headers: {
                    "Authorization": "Bearer " + jwtToken
                }
            };
            const response = await fetch(
                `${API_URL}:${API_PORT}/api/v1/ingredients`,
                headers);
            return await response.json();
        } catch (e) {
            throw new Error(e);
        }
    }
};

export default IngredientService;