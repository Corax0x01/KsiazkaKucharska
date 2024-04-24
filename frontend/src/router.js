import {createWebHistory, createRouter } from "vue-router";
import HomeView from "@/views/HomeView.vue";
import UsersView from "@/views/user/UsersView.vue";
import RecipesView from "@/views/recipe/RecipesView.vue";
import RecipeView from "@/views/recipe/RecipeView.vue";
import CreateRecipeView from "@/views/recipe/CreateRecipeView.vue";
import CreateUserView from "@/views/user/CreateUserView.vue";
import NotFoundView from "@/views/NotFoundView.vue";

const routes = [
    {path: '/', name: 'home', component: HomeView},
    {path: '/home', redirect: "/"},
    {path: '/users', name: 'users', component: UsersView},
    {path: '/create-user', name: 'create user', component: CreateUserView},
    {path: '/recipes', name: 'recipes', component: RecipesView},
    {path: '/recipes/:id', name: 'recipe', component: RecipeView},
    {path: '/create-recipe', name: 'create recipe', component: CreateRecipeView},
    {path: "/:catchall(.*)*", name: "not found", component: NotFoundView}
]

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes
})

export default router;