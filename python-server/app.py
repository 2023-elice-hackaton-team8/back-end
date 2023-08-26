from flask import Flask
from ocr import OCR

app = Flask(__name__)
vision_client = OCR()

# https://flask.palletsprojects.com/en/2.3.x/quickstart/
@app.route('/')
def index():
    return 'Index Page'

@app.route('/hello')
def hello():
    return 'Hello, World'

@app.route('/ocr')
def ocr():
    # TODO: replace path with real files or image URI
    return vision_client.get_text("test2.jpg")