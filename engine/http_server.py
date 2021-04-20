from http.server import HTTPServer, BaseHTTPRequestHandler


handler = BaseHTTPRequestHandler


class ComputerRequestHandler(BaseHTTPRequestHandler):
    self.responses =


port = 8000
def run(server_class=HTTPServer, handler_class=BaseHTTPRequestHandler):
    server_address = ('', port)
    httpd = server_class(server_address, handler_class)
    httpd.serve_forever()

run()