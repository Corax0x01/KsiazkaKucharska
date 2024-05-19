const API_URL = import.meta.env.VITE_API_URL;
const API_PORT = import.meta.env.VITE_API_PORT;
const ITEMS_ON_PAGE = 4;

const RecipeService = {
    getRecipesData: async function (page, jwtToken) {
        try {
            const headers = {
                headers: {
                    "Authorization": "Bearer " + jwtToken
                }
            };
            const response = await fetch(
                `${API_URL}:${API_PORT}/api/v1/recipes?size=${ITEMS_ON_PAGE}&page=${page}`,
                headers
            );
            const json = await response.json();
            return json["content"];
        } catch (e) {
            throw new Error(e);
        }
    },
    getRecipeData: async function (recipeId, jwtToken) {
      try {
          const headers = {
              headers: {
                  "Authorization": "Bearer " + jwtToken
              }
          };
          const response = await fetch(
              `${API_URL}:${API_PORT}/api/v1/recipes/${recipeId}`,
              headers);
          return await response.json();
      }  catch (e) {
          throw new Error(e);
      }
    },
    getRecipeIngredients: async function (recipeId, jwtToken) {
      try {
          const headers = {
              headers: {
                  "Authorization": "Bearer " + jwtToken
              }
          };
          const response = await fetch(
              `${API_URL}:${API_PORT}/api/v1/recipes/${recipeId}/ingredients`,
              headers);
          return await response.json();
      }  catch (e) {
          throw new Error(e);
      }
    },
    createRecipe: async function (recipe, ingredients, jwtToken) {
        try {
            const recipeBody = {...recipe};
            const ingredientsBody = {...ingredients};

            const body = {
              "recipe": JSON.stringify(recipeBody),
              "ingredients": JSON.stringify(ingredientsBody)
            };

            const requestOptions = {
                method: 'POST',
                body: JSON.stringify(body),
                headers: {
                    "Authorization": "Bearer " + jwtToken,
                    "Content-Type": "application/json"
                }
            }

            const response = await fetch(`${API_URL}:${API_PORT}/api/v1/recipes`, requestOptions);
            return response.json();
        } catch (e) {
            throw new Error(e);
        }
    },
    getAllTags: async function (jwtToken) {
        try {
            const headers = {
                headers: {
                    "Authorization": "Bearer " + jwtToken
                }
            };
            const response = await fetch(
                `${API_URL}:${API_PORT}/api/v1/recipes/tags`,
                headers);
            return await response.json();
        } catch (e) {
            throw new Error(e);
        }
    },
    getNumberOfPages: async function (jwtToken) {
        try {
            const headers = {
                headers: {
                    "Authorization": "Bearer " + jwtToken
                }
            };
            const response = await fetch(
                `${API_URL}:${API_PORT}/api/v1/recipes?size=${ITEMS_ON_PAGE}`,
                headers);
            const json = await response.json();
            return await json["totalPages"];
        } catch (e) {
            throw new Error(e);
        }
    }
};

export default RecipeService;