import { defineOverridesPreferences } from '@vben/preferences';

/**
 * @description 项目配置文件
 * 只需要覆盖项目中的一部分配置，不需要的配置不用覆盖，会自动使用默认配置
 * !!! 更改配置后请清空缓存，否则可能不生效
 */
export const overridesPreferences = defineOverridesPreferences({
  // overrides

  app: {
    name: import.meta.env.VITE_APP_TITLE,
    loginExpiredMode: 'modal',
  },
  copyright: {
    date: 'Vben Admin',
    companyName: 'Vben Admin22',
    companySiteLink: 'https://github.com/vbenjs/vben-admin',
    enable: true,
    icp: 'ICP 0000000000',
    icpLink: 'https://beian.miit.gov.cn',
  },
});
