# 部署指南

本指南将帮助您完成 Quick Start 项目的前端和后端部署。请确保您已安装必要的工具和依赖项。

## 前端部署

前端项目使用 Vben 作为开发框架，以下是部署步骤：

1. **安装依赖**：
   确保您已安装 [pnpm](https://pnpm.io/installation)。在项目根目录下运行以下命令安装依赖：
   ```bash
   pnpm install
   ```

2. **构建项目**：
   使用以下命令构建前端项目：
   ```bash
   pnpm build
   ```
   这将生成一个 `dist` 目录，其中包含可部署的静态文件。

3. **部署静态文件**：
   将 `dist` 目录中的文件上传到您的 Web 服务器（如 Nginx、Apache）或云服务（如 Vercel、Netlify）。

## 后端部署

后端项目基于 Java Spring Boot，使用 Maven 进行构建和部署。以下是步骤：

1. **安装 Maven**：
   确保您已安装 [Maven](https://maven.apache.org/install.html) 和 JDK。

2. **构建项目**：
   在项目根目录下运行以下命令构建项目：
   ```bash
   mvn clean package
   ```
   这将生成一个可执行的 JAR 文件，通常位于 `target` 目录中。

3. **运行 JAR 文件**：
   使用以下命令运行生成的 JAR 文件：
   ```bash
   java -jar target/your-app-name.jar
   ```
   确保您已配置好应用程序的环境变量和配置文件。

4. **部署到服务器**：
   您可以将 JAR 文件部署到任何支持 Java 的服务器（如 Tomcat、Jetty）或使用 Docker 容器化部署。

## 结语

通过以上步骤，您可以成功部署 Quick Start 项目的前端和后端。请根据您的具体需求调整配置和部署策略。若有任何问题，请参考官方文档或社区支持。 