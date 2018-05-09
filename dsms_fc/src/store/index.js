import Vue from 'vue'
import Vuex from 'vuex'

import schedule from './schedule/index.js'
import media from './media/index.js'
import template from './template/index.js'

import user from './user/index.js'
import device from './device/index.js'

Vue.use(Vuex);

const store = new Vuex.Store({
	modules: {
		media: media,
		schedule: schedule,
		template: template,
		user: user,
		device: device,
	},
});

export default store

