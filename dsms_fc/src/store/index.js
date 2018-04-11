import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

const store = new Vuex.Store({
	state: {
		show: {
			login: false,
			logout: false,
			templateNew: false,
			mediaNew: false,
			scheduleEdit: false,
		},
		loginState: false,
		centerSelect: 'info',
		logoutCode: 10,
		userId: '',
	}
});

export default store

