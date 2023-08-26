set -euxo pipefail

python3 -m venv env
source env/bin/activate
pip install -r requirements.txt
python -m flask run