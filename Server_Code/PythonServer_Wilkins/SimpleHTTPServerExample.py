import http.server
import socketserver
import ssl

PORT = 8000

Handler = http.server.SimpleHTTPRequestHandler

httpd = socketserver.TCPServer(("", PORT), Handler)

httpd.socket = ssl.wrap_socket(httpd.socket, certfile='./server.pem', server_side=True)

print("serving at port", PORT)
httpd.serve_forever()
