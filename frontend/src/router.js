import {createWebHistory, createRouter } from "vue-router";
import {useAuthStore} from "@/stores/AuthStore.js";
import HomeView from "@/views/HomeView.vue";
import UsersView from "@/views/user/UsersView.vue";
import RecipesView from "@/views/recipe/RecipesView.vue";
import RecipeView from "@/views/recipe/RecipeView.vue";
import CreateRecipeView from "@/views/recipe/CreateRecipeView.vue";
import CreateUserView from "@/views/user/CreateUserView.vue";
import RegisterView from "@/views/auth/RegisterView.vue";
import LogInView from "@/views/auth/LogInView.vue";
import NotFoundView from "@/views/NotFoundView.vue";
import ActivateAccountView from "@/views/auth/ActivateAccountView.vue";

const routes = [
    {path: '/', name: 'home', component: HomeView},
    {path: '/home', redirect: "/"},
    {path: '/users', name: 'users', component: UsersView},
    {path: '/create-user', name: 'create user', component: CreateUserView},
    {path: '/recipes', name: 'recipes', component: RecipesView},
    {path: '/recipes/:id', name: 'recipe', component: RecipeView},
    {path: '/create-recipe', name: 'create recipe', component: CreateRecipeView},
    {path: '/login', name: 'login', component: LogInView},
    {path: '/register', name: 'register', component: RegisterView},
    {path: '/activate-account', name: 'activate account', component: ActivateAccountView},
    {path: "/:catchall(.*)*", name: "not found", component: NotFoundView}
]

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes
})

router.beforeEach(async (to, from) => {
    const allowedPaths = ["login", 'register', 'home', 'activate account'];

    // if not authenticated and path not in allowedPaths, redirect to login page
    if(!allowedPaths.includes(to.name) &&
        !useAuthStore().authenticated) {
        return { name: 'login'};
    }
});

export default router;