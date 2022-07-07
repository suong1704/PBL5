import firebase_admin
from firebase_admin import credentials, messaging
from firebase_admin import db
import time
import numpy as np
import pandas as pd
import csv
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression
import json
from datetime import datetime
from sklearn.metrics import accuracy_score


cred = credentials.Certificate("fb_sdk.json")
firebase_admin.initialize_app(cred,{
    'databaseURL' : 'https://esp8266-72053-default-rtdb.firebaseio.com'
})
# ref = db.reference('device/device02/heartbeat/')


list_heartbeat = []


def read_data():
    ref = db.reference('device/device01/heartbeat/')

    data = pd.DataFrame(ref.get())
    for res in data.iloc[:1].values:
        return res
    # print(ref.get())

    print()


def hanlde_data():
    list_heartbeat = read_data()
    print("list data in handle data")
    print(list_heartbeat)
    for x in list_heartbeat:
        training(int(x))


def training(value):
    # read data in dataset
    heart_data = pd.read_csv('heart.csv')

    # split data into 2 data frame to training
    X = heart_data[['age', 'trtbps']]
    Y = heart_data[['output']]

    # train model
    X_train, X_test, Y_train, Y_test = train_test_split(
        X, Y, test_size=0.2, stratify=Y, random_state=2)
    model = LogisticRegression()
    model.fit(X_train, Y_train.values.ravel())
    X_train_prediction = model.predict(X_train)
    training_data_accuracy = accuracy_score(X_train_prediction, Y_train)

    X_test_prediction = model.predict(X_test)
    test_data_accuracy = accuracy_score(X_test_prediction, Y_test)
    print('Accuracy on Test data : ', test_data_accuracy)

    input_data = (value, 0)
    input_data_as_numpy_array = np.asarray(input_data)
    input_data_reshaped = input_data_as_numpy_array.reshape(1, -1)
    prediction = model.predict(input_data_reshaped)
    print(prediction)

    if (prediction[0] == 0):
        print('The Person does not have a Heart Disease')
    else:
        send_data(1, value)


def send_data(value, heartbeat):
    sendata = db.reference('device/device02')
    time.sleep(20)
    if(value == 1):
        sendata.child("message").child(get_date_time()).update(
            {'message': 'nhịp tim bất thường: ' + str(heartbeat)})


def get_date_time():
    now = datetime.now()
    year = now.strftime("%Y")
    month = now.strftime("%m")
    day = now.strftime("%d")
    time = now.strftime("%H:%M:%S")
    res_date = str(year) + '-' + str(month) + '-' + str(day) + 'T' + time + 'Z'

    return res_date



while(True):
    hanlde_data()
    time.sleep(20)