import dt_middle
import json
from http.server import HTTPServer, BaseHTTPRequestHandler

data = {
    'code': 0,
    'msg': 'Success',
    'timestamp': '',
    'data': {'request_id': '25d55ad283aa400af464c76d713c07ad'}
}
host = ('localhost', 9090)


# start push
def start(self):
    content_length = int(self.headers['Content-Length'])
    read_data = self.rfile.read(content_length)
    post_data = json.loads(read_data)
    return dt_middle.start(post_data)


# stop push
def stop(self):
    content_length = int(self.headers['Content-Length'])
    read_data = self.rfile.read(content_length)
    post_data = json.loads(read_data)
    return dt_middle.stop(post_data)


methods = {
    '/camera/start': start,
    '/camera/stop': stop
}


class Request(BaseHTTPRequestHandler):
    def do_HEAD(self):
        self.send_response(200)
        self.send_header('Content-type', 'application/json')
        self.end_headers()

    def do_GET(self):
        if self.path == '/camera/running':
            self.do_HEAD()
            self.wfile.write(json.dumps({
                'code': 0,
                'data': dt_middle.running()
            }).encode())
        else:
            self.do_HEAD()
            self.wfile.write(json.dumps({
                'usage': [
                    'Get - /camera/running',
                    'Post - /camera/start',
                    'Post - /camera/stop']
            }).encode())

    def do_POST(self):
        paths = ['/camera/start', '/camera/stop']
        if self.path in paths:
            m = methods[self.path]
            r = m(self)
            return self.do_R(r, None)
        else:
            self.send_error(404, "no paths confirm to")
            return
        # logging.info("POST request,\nPath: %s\nHeaders:\n%s\n\nBody:\n%s\n", str(self.path), str(self.headers),
        # post_data.decode('utf-8'))

    def do_R(self, f, d):
        self.do_HEAD()
        if f:
            self.wfile.write(json.dumps({
                'code': 0,
                'data': d
            }).encode('utf-8'))
        else:
            self.wfile.write(json.dumps({
                'code': 1,
                'data': d
            }).encode('utf-8'))


# if __name__ == '__main__':
print("Starting http server, listen at: %s:%s" % host)
server = HTTPServer(host, Request)
print("Starting http server, listen at: %s:%s" % host)
server.serve_forever()
