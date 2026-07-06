import type { RouteRecordRaw } from 'vue-router';
import BuyerHome from '../pages/buyer/BuyerHome.vue';
import CreatorHome from '../pages/creator/CreatorHome.vue';
import AdminHome from '../pages/admin/AdminHome.vue';

export const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'buyer-home',
    component: BuyerHome
  },
  {
    path: '/creator',
    name: 'creator-home',
    component: CreatorHome
  },
  {
    path: '/admin',
    name: 'admin-home',
    component: AdminHome
  }
];
