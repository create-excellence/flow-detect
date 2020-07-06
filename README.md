# Flow Detect 🏄‍♀️



- dt_server.py 



```python
from http.server import HTTPServer, BaseHTTPRequestHandler
import json
import dt_middle

data = {
    'code': 0,
    'msg': 'Success',
    'timestamp': '',
    'data': {'request_id': '25d55ad283aa400af464c76d713c07ad'}
}
host = ('localhost', 8888)


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


if __name__ == '__main__':
    server = HTTPServer(host, Request)
    print("Starting http server, listen at: %s:%s" % host)
    server.serve_forever()

```




- dt_middle.py



```python
import datetime
from threading import Thread
import dt_send

cameras = []


class StartPushThread(Thread):
    def __init__(self, n):
        super(StartPushThread, self).__init__()
        self.n = n

    def run(self):
        global cameras
        cameras.append(self.n)
        while self.n in cameras:
            print("start;;push;;detect;;")
            # 1. Start push
            # 2. Return detect data
            # 3. Send request to save data
            dt_send.save_detect_data({
                'cid': self.n,
                'number': 'detect data',
                'timestamp': datetime.datetime.now()
            })


def start(params):
    print("cid", params['cid'])
    StartPushThread(params['cid']).start()
    return True


def stop(params):
    global cameras
    print("stop {}".format(params['cid']))
    if params['cid'] in cameras:
        cameras.remove(params['cid'])
        return True
    else:
        print("current cid not in list:", params)
        return False


# get running thread - return count
def running():
    global cameras
    return cameras

```


- dt_send.py



```python
import requests
import json
import datetime

url = ['http://localhost:8888/camera/start',
       'http://localhost:8888/camera/stop',
       'http://localhost:8888/camera/running']

demo = {
    'cid': 1,
    'number': 1,
    'timestamp': datetime.datetime.now()
}


def save_detect_data(data):
    response = requests.post(url[1], json.dumps(data, cls=DateEncoder).encode())
    response_data = json.loads(response.text)
    print("Success") if response_data['code'] == 0 else \
        print("Save detect data error : cid = {} ".format(response_data['code']))


# datetime json serializable
class DateEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, datetime.datetime):
            return obj.strftime('%Y-%m-%d %H:%M:%S')

        elif isinstance(obj, datetime.date):
            return obj.strftime("%Y-%m-%d")

        else:
            return json.JSONEncoder.default(self, obj)

```
