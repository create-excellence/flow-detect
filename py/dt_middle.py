import threading
import datetime
import requests
import json
import cv2
import numpy as np
import subprocess as sp
from PIL import Image
from yolo import YOLO
import yolo_video1

yolo = YOLO()

cameras = []
shared_image = (np.ones((540, 960, 3), dtype=np.uint8) * 255).astype(np.uint8)
process_image = (np.ones((540, 960, 3), dtype=np.uint8) * 255).astype(np.uint8)
people_count = 0
rtmpUrl = "rtmp://live.imhtb.cn/live/1?txSecret=3ed5b1ee0851161fb008b90fe79259e8&txTime=5EFA958A"
source = "rtsp://admin:qwe123123@10.21.123.31:554/h264/ch1/main/av_stream"
command = ['ffmpeg',
           '-y',
           '-f', 'rawvideo',
           '-vcodec', 'rawvideo',
           '-pix_fmt', 'bgr24',
           '-s', '{}x{}'.format(960, 540),
           '-r', str(24),
           '-i', '-',
           '-c:v', 'libx264',
           '-pix_fmt', 'yuv420p',
           '-preset', 'ultrafast',
           '-f', 'flv',
           rtmpUrl]


class CapThread(threading.Thread):
    def __init__(self, n, s):
        super(CapThread, self).__init__()
        self.n = n
        self.s = s

    def run(self):
        global cameras
        global shared_image
        cameras.append(self.n)
        # Create pipe
        pipe = sp.Popen(command, stdin=sp.PIPE)

        # init cap
        cap = cv2.VideoCapture(source)
        cap.set(cv2.CAP_PROP_FRAME_WIDTH, 960)
        cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 540)

        # Get video information
        fps = int(cap.get(cv2.CAP_PROP_FPS))
        width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
        height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))

        while cap.isOpened() and self.n in cameras:
            back, frame = cap.read()
            if frame is None:
                if self.n in cameras:
                    cameras.remove(self.n)
                return
            print("start;;push;;detect;;")
            # 1. Start push
            image = Image.fromarray(frame)
            image = image.resize((960, 540))
            frame = np.asarray(image)
            shared_image = frame

            try:
                pipe.stdin.write(shared_image.tobytes())
            except Exception as e:
                if self.n in cameras:
                    cameras.remove(self.n)
                print('Error:', e)


class DetectThread(threading.Thread):
    def __init__(self, n):
        super(DetectThread, self).__init__()
        self.n = n

    def run(self):
        print('detect thread is run!')
        global cameras
        global shared_image
        global process_image
        global people_count
        while self.n in cameras:

            image = Image.fromarray(shared_image)
            [frame, pc] = yolo_video1.detect_img(yolo, image)
            process_image = np.asarray(frame)
            print("count:" + str(pc))
            people_count = pc
            # Send request to save data
            save_detect_data({
                'cameraId': self.n,
                'flow': people_count,
                'timestamp': datetime.datetime.now()
            })
            # time.sleep(0.05)


def start(params):
    print("cid", params['cid'])
    # 开启截帧线程 参数摄像头id
    CapThread(['cid'], params["source"]).start()
    # 开启检测线程 参数摄像头id
    DetectThread(params['cid']).start()
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


def save_detect_data(data):
    response = requests.post("http://localhost:8888/api/v1/flow", json.dumps(data, cls=DateEncoder).encode())
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