import Vue from 'vue'
import VueRouter from 'vue-router'

import FirstPage from '../components/FirstPage.vue'

import Schedule from '../components/schedule/Schedule.vue'

import Template from '../components/template/Template.vue'
import TemplateEdit from '../components/template/TemplateEdit.vue'

import Media from '../components/media/Media.vue'

import UserCenter from '../components/user/UserCenter.vue'

import index from '../components/index.vue'

Vue.use(VueRouter);

const router = new VueRouter({
	routes:
		[
			{
				path: '/', component: index,
				children: [
					{path: '', redirect: 'first'},
					{path: 'first', component: FirstPage},
					{path: 'schedule', component: Schedule},
					{path: 'template', component: Template},
					{path: 'templateEdit/:id', component: TemplateEdit},
					{path: 'media', component: Media},
				],
			},
      {path: '/userCenter', component: UserCenter},
		]
});

export default router
