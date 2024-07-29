# Stage 1: Build the React application
FROM node:16 AS build

# Set the working directory
WORKDIR /app

# Copy the package.json and package-lock.json (if available)
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Build the application
RUN npm run build

# Stage 2: Serve the application with Node.js
FROM node:16-alpine

# Set the working directory
WORKDIR /app

# Install the 'serve' package to serve the built application
RUN npm install -g serve

# Copy the build output from the previous stage
COPY --from=build /app/dist ./dist

# Expose port 3000 to the outside world
EXPOSE 3000

# Serve the application
CMD ["serve", "-s", "dist", "-l", "3000"]
