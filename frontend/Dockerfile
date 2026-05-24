# Stage 1 – Build Angular app
FROM node:20-alpine AS build

WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY . .
RUN npm run build -- --configuration production

# Stage 2 – Serve with Nginx
FROM nginx:alpine

# Remove default Nginx page
RUN rm -rf /usr/share/nginx/html/*

# Copy built Angular output (adjust 'browser' path if your dist folder differs)
COPY --from=build /app/dist/*/browser /usr/share/nginx/html

# Nginx config that supports Angular routing (HTML5 pushState)
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]