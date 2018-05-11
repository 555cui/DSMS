import Vue from 'vue'
import VueRouter from 'vue-router'

import index from '../components/index.vue'

Vue.use(VueRouter);

const FirstPage = r => require([ '../components/FirstPage.vue'], r);
const Schedule = r=> require(['../components/schedule/Schedule.vue'], r);
const Template = r=> require(['../components/template/TemplateShow.vue'], r);
const TemplateEdit = r=> require(['../components/template/TemplateEdit.vue'], r);
const Media = r=> require(['../components/media/Media.vue'], r);
const UserCenter = r=> require(['../components/user/UserCenter.vue'], r);
const Playing = r=> require(['../components/playing/Playing.vue'], r);

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
					{path: 'template/:id', component: TemplateEdit},
					{path: 'media', component: Media},
				],
			},
			{path: '/userCenter', component: UserCenter},
			{path: '/playing', component: Playing},
		]
});

export default router
