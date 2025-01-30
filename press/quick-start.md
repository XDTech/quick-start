# 欢迎使用 Quick Start

## 介绍

Quick Start 是一个快速开发文档的工具，旨在帮助开发者快速搭建和运行项目。

## 特性

- **快速上手**：只需几分钟即可搭建并运行你的项目。
- **现代技术栈**：使用最新的技术栈，如 Vite、Vue 3 等。
- **高性能**：优化的性能，确保你的项目运行流畅。
- **丰富的插件**：支持多种插件，满足不同需求。
- **易于扩展**：灵活的架构，方便扩展和定制。
- **社区支持**：活跃的社区，提供丰富的资源和支持。

## 后端技术

- **Spring Boot 3**：现代化的 Java 后端框架，提供快速开发和部署的能力。

## 开始使用

1. 安装依赖：
   ```sh
   pnpm install 
   ```

2. 使用 Spring Boot 3 创建后端服务：

   - 创建一个新的 Spring Boot 项目：
     ```sh
     spring init --dependencies=web my-springboot-app
     cd my-springboot-app
     ```

   - 运行 Spring Boot 应用：
     ```sh
     ./mvnw spring-boot:run
     ```

   - 访问应用：
     打开浏览器并访问 `http://localhost:8080`。 