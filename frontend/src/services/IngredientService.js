const API_URL = import.meta.env.VITE_API_URL;
const API_PORT = import.meta.env.VITE_API_PORT;

const IngredientService = {
    getAllIngredients: async function (){
        try {
            const response = await fetch(`${API_URL}:${API_PORT}/api/ingredients`);
            return await response.json();
        } catch (e) {
            throw new Error(e);
        }
    }
};

export default IngredientService;