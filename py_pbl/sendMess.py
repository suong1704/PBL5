from firebase_admin import messaging
tokens = ["AAAAgLvBH60:APA91bHqua59i0yn7zyn13ziVDOcOJkd4zr_mZXVisVGozWTbZW1tFlBTJ3Qdxo9AI1clF66bLT0cxujWJF4_wnM-ihPi485PpoJMNwioIihBrgXK2vMA1PafCBf1SprLMwECjcpewm-"]

message = messaging.Message(
    data={
        'score': '850',
        'time': '2:45',
    },
    token=tokens,
)
messaging.send(message)