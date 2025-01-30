import { defineConfig } from "vitepress";

// https://vitepress.dev/reference/site-config
export default defineConfig({
  title: "Quick Start",
  description: "快速开发文档",
  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    nav: [
      { text: "Home", link: "/" },
      // { text: "Examples", link: "/markdown-examples" },
    ],

    sidebar: [
      {
        text: "指南",
        items: [
          { text: "介绍", link: "/quick-start" },
          { text: "配置", link: "/quick-start/config" },
          { text: "插件", link: "/plugins" },
          { text: "部署", link: "/deploy" },
        ],
      },
      {
        text: "前端使用",
        items: [
          { text: "项目概览", link: "/project-overview" },
          { text: "权限", link: "/permission" },
          // 可以在这里添加更多的前端相关链接
        ],
      },
      {
        text: "后端使用",
        items: [
          { text: "Lombok", link: "/lombok" },
          { text: "EasyCode", link: "/code-generate" },
          { text: "项目概览", link: "/backend-project-overview" },
          // 可以在这里添加更多的后端相关链接
        ],
      },
    ],

    socialLinks: [
      { icon: "github", link: "https://github.com/XDTech/quick-start" },
    ],
  },
});
