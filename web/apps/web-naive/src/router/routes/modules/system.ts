import type { RouteRecordRaw } from 'vue-router';

import { BasicLayout } from '#/layouts';

const routes: RouteRecordRaw[] = [
  {
    component: BasicLayout,
    meta: {
      icon: 'material-symbols:settings-outline-rounded',
      order: 1,
      title: '系统设置',
    },
    name: 'system',
    path: '/system',
    children: [
      {
        name: '用户管理',
        path: '/system/user',
        component: () => import('#/views/dashboard/analytics/index.vue'),
        meta: {
          icon: 'lucide:users-round',
          title: '用户管理',
        },
      },
      {
        name: '菜单权限',
        path: '/system/permission',
        component: () => import('#/views/system/permission/index.vue'),
        meta: {
          icon: 'ic:outline-health-and-safety',
          title: '菜单权限',
        },
      },
      {
        name: '角色管理',
        path: '/system/role',
        component: () => import('#/views/system/role/index.vue'),
        meta: {
          icon: 'carbon:user-role',
          title: '角色管理',
        },
      },
      {
        name: '部门管理',
        path: '/system/department',
        component: () => import('#/views/system/department/index.vue'),
        meta: {
          icon: 'clarity:organization-line',
          title: '部门管理',
        },
      },
    ],
  },
];

export default routes;
