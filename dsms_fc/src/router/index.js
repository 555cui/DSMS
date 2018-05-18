import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter);

const index = r=> require(['../components/index.vue'], r);
const Schedule = r=> require(['../components/schedule/Schedule.vue'], r);
const ScheduleList = r=> require(['../components/schedule/ScheduleList.vue'], r);
const Template = r=> require(['../components/template/TemplateShow.vue'], r);
const TemplateEdit = r=> require(['../components/template/TemplateEdit.vue'], r);
const Media = r=> require(['../components/media/Media.vue'], r);
const DeviceList = r=> require(['../components/device/DeviceList.vue'], r);
const UserCenter = r=> require(['../components/user/UserCenter.vue'], r);
const Playing = r=> require(['../components/playing/Playing.vue'], r);

const router = new VueRouter({
	mode: 'history',
	routes:
		[
			{
				path: '/', component: index,
				children: [
					{path: '', redirect: 'first'},
					{path: 'first', component: Schedule},
					{path: 'schedule', component: ScheduleList},
					{path: 'template', component: Template},
					{path: 'template/:id', component: TemplateEdit},
					{path: 'media', component: Media},
          {path: 'device', component: DeviceList},
				],
			},
			{path: '/userCenter', component: UserCenter},
			{path: '/playing', component: Playing},
		]
});

export default router
