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
          { text: "项目概览", link: "/quick-start" },
          { text: "构建与部署", link: "/deployment-guide" },
        ],
      },
      {
        text: "前端使用",
        items: [
     
          { text: "快速开始", link: "/vue/operation" },
          { text: "权限", link: "/vue/permission" },
          // 可以在这里添加更多的前端相关链接
        ],
      },
      {
        text: "后端使用",
        items: [
          { text: "Lombok", link: "/springboot/lombok" },
          { text: "EasyCode", link: "/springboot/code-generate" }
       
          // 可以在这里添加更多的后端相关链接
        ],
      },
    ],

    socialLinks: [
      { icon: "github", link: "https://github.com/XDTech/quick-start" },
    ],
  },
});
