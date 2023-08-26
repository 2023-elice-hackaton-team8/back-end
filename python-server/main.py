from flask import Flask
from flask import request
import urllib.parse
from ocr import OCR
import os

app = Flask(__name__)
vision_client = OCR()


@app.route("/")
def index():
    return "Index Page"


@app.route("/ocr", methods=["POST"])
def ocr():
    # Example: https://ojy1-bucket.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20230826_230308602.jpg
    content_type = request.headers.get("Content-Type")

    if content_type == "application/json":
        uri = request.json["uri"]
        return vision_client.get_text_from_uri(uri)
    else:
        return "Content type is not supported."


if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=int(os.environ.get("PORT", 8080)))
