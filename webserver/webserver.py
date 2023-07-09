import http.server
import socketserver
import os

# Set the directory where your files are located
directory = "serve/client"

# Set the port number for the server
port = 8000

# Create a handler to serve the files
handler = http.server.SimpleHTTPRequestHandler

# Change to the specified directory
os.chdir(directory)

# Start the server
with socketserver.TCPServer(("", port), handler) as httpd:
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        httpd.server_close()
        print("Server stopped.")