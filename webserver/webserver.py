import http.server
import socketserver
import os

# Set the directory where your files are located
directory = "serve/"

# Set the port number for the server
port = 8000

# Change to the specified directory
os.chdir(directory)

# Create a ThreadingHTTPServer to serve the files
class ThreadedHTTPRequestHandler(http.server.SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)

# Start the server
with socketserver.ThreadingTCPServer(("", port), ThreadedHTTPRequestHandler) as httpd:
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        httpd.server_close()
        print("Server stopped.")
