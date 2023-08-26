set -euxo pipefail

export FLASK_APP=main.py
export GOOGLE_APPLICATION_CREDENTIALS=google_secret.json
python3 -m venv env
source env/bin/activate
pip install -r requirements.txt
python -m flask run