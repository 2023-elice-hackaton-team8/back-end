from flask import Flask
from ocr import OCR

app = Flask(__name__)
vision_client = OCR()


# https://flask.palletsprojects.com/en/2.3.x/quickstart/
@app.route("/")
def index():
    return "Index Page"


@app.route("/hello")
def hello():
    return "Hello, World"


@app.route("/ocr")
def ocr():
    # TODO: replace path with real files or image URI
    # return vision.client.get_text_from_uri("https://DOC-EXAMPLE-BUCKET1.s3.us-west-2.amazonaws.com/text.png")
    return vision_client.get_text_from_path("test2.jpg")
