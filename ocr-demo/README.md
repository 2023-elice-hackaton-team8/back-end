1. Install python 3 and virtualenv.
2. Copy `google_secret.json` file with service account credentials.

```
source environment  # sets $GOOGLE_APPLICATION_CREDENTIALS
python3 -m venv env
source env/bin/activate
pip install -r requirements.txt
python3 ocr.py
```