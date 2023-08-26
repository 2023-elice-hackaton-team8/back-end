from google.cloud import vision


# Uses service account credentials file set at GOOGLE_APPLICATION_CREDENTIALS env var.
client = vision.ImageAnnotatorClient()

def detect_document(path):
    with open(path, "rb") as image_file:
        content = image_file.read()
    image = vision.Image(content=content)

    response = client.document_text_detection(image=image)

    result = []
    for page in response.full_text_annotation.pages:
        for block in page.blocks:
            for paragraph in block.paragraphs:
                result += ["\n"]
                for word in paragraph.words:
                    word_text = "".join([symbol.text for symbol in word.symbols])
                    result.append(word_text)
    
    print("".join(result))

    

detect_document("image.jpg")