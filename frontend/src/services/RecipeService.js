const API_URL = import.meta.env.VITE_API_URL;
const API_PORT = import.meta.env.VITE_API_PORT;
const ITEMS_ON_PAGE = 4;

const RecipeService = {
    getRecipesData: async function (page) {
        try {
            const response = await fetch(`${API_URL}:${API_PORT}/api/recipes?size=${ITEMS_ON_PAGE}&page=${page}`);
            const json = await response.json();
            return json["content"];
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
    },
    getNumberOfPages: async function () {
        try {
            const response = await fetch(`${API_URL}:${API_PORT}/api/recipes?size=${ITEMS_ON_PAGE}`);
            const json = await response.json();
            return await json["totalPages"];
        } catch (e) {
            throw new Error(e);
        }
    }
};

export default RecipeService;