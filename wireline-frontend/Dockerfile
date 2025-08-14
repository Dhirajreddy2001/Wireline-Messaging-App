# Base image
FROM node:20

# Set working directory
WORKDIR /app

# Copy source code
COPY . .

# Install dependencies
RUN npm install

# Build static assets
RUN npm run build

# Install static file server (serve)
RUN npm install -g serve

# Run production build with serve
CMD ["serve", "-s", "dist"]
