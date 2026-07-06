FROM nginx:1.27-alpine
COPY skill-store/frontend/dist /usr/share/nginx/html
EXPOSE 80
