from flask import Flask
from flask import request
import urllib.parse
from ocr import OCR
from feedback import Feedback
import os

app = Flask(__name__)
vision_client = OCR()
feedback_client = Feedback()


@app.route("/")
def index():
    return "Index Page"

@app.route("/feedback", methods=["POST"])
def feedback():
    # {
    #     "problem_type": true,
    #     "question": "question goes here",
    #     "answer": "answer goes here",
    #     "wrong_answer": "wrong answer goes here",
    # }
    problem_type = request.json.get("problem_type", "")
    question = request.json.get("question", "")
    answer = request.json.get("answer", "")
    wrong_answer = request.json.get("wrong_answer", "")
    
    wrong_answer.replace("WRONGANSWER=", "")

    feedback = feedback_client.return_feedback(
        prob_type=problem_type,
        QUESTION=question,
        ANSWER=answer,
        WRONGANSWER=wrong_answer
    )

    return feedback

# TODO: Consider adding error handlers
# https://flask.palletsprojects.com/en/2.3.x/errorhandling/#returning-api-errors-as-json
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
