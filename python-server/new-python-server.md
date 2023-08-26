Deployed at: https://python-server-mphf6a2egq-du.a.run.app

# OCR Endpoint

Pass image URI as form data.
```
curl --location 'https://python-server-mphf6a2egq-du.a.run.app/ocr' \
--form 'uri="https://ojy1-bucket.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20230826_230308602.jpg"'
```

# Local Deploy
1. Get a copy of `google_secret.json` and place in this directory.
2. `./run_python_server.sh`.

# Deploy to Cloud Run
See https://cloud.google.com/run/docs/quickstarts/build-and-deploy/deploy-python-service

`gcloud run deploy --source .`



