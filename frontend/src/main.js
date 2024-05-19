import './assets/main.css'
import {createApp} from 'vue'
import {createPinia} from "pinia";
import App from './App.vue'
import router from './router'
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";

createApp(App)
    .use(router)
    .use(createPinia())
    .component('font-awesome-icon', FontAwesomeIcon)
    .mount('#app')
