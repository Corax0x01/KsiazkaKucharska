const API_URL = import.meta.env.VITE_API_URL;
const API_PORT = import.meta.env.VITE_API_PORT;

const RecipeService = {
    getRecipesData: async function () {
        try {
            const response = await fetch(`${API_URL}:${API_PORT}/api/recipes`);
            return await response.json();
        } catch (e) {
            throw new Error(e);
        }
    },
    getRecipeData: async function (recipeId) {
      try {
          const response = await fetch(`${API_URL}:${API_PORT}/api/recipes/${recipeId}`);
          return await response.json();
      }  catch (e) {
          throw new Error(e);
      }
    },
    getRecipeIngredients: async function (recipeId) {
      try {
          const response = await fetch(`${API_URL}:${API_PORT}/api/recipes/${recipeId}/ingredients`);
          return await response.json();
      }  catch (e) {
          throw new Error(e);
      }
    },
    createRecipe: async function (recipe, ingredients) {
        try {
            let recipeBody = {...recipe};
            let ingredientsBody = {...ingredients};

            const body = {
              "recipe": recipeBody,
              "ingredients": ingredientsBody
            };

            const requestOptions = {
                method: 'POST',
                body: JSON.stringify(body),
                headers: {
                    "Content-Type": "application/json"
                }
            }

            const response = await fetch(`${API_URL}:${API_PORT}/api/recipes`, requestOptions);
            return response.json();
        } catch (e) {
            throw new Error(e);
        }
    },
    getAllTags: async function () {
        try {
            const response = await fetch(`${API_URL}:${API_PORT}/api/recipes/tags`);
            return await response.json();
        } catch (e) {
            throw new Error(e);
        }
    }
};

export default RecipeService;