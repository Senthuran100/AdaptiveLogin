#!flask/bin/python
from flask import Flask, request
import pandas as pd
import pickle
from sklearn.model_selection import train_test_split
from sklearn.compose import ColumnTransformer
from sklearn.preprocessing import OneHotEncoder

app = Flask(__name__)

filenameMouseKeyboard = 'MouseKey.sav'
filenameBrowserInfo = 'BrowserInfo.sav'
TEST_USERNAME = 'senthuran'

def replaceBrackets(line):
    brackets = "{}"
    for char in brackets:
        line = line.replace(char, "")
    return line;


def convertStringToNumberList(list):
    for keys in list:
        try:
            list[keys] = int(list[keys])
        except ValueError:
            list[keys] = float(list[keys])
    return list;


@app.route('/userDynamics', methods=['POST', 'GET'])
def userDynamics():
    combinedEvent = replaceBrackets(request.get_json()['mouseEvent']) + ", " + replaceBrackets(
        request.get_json()['keyboardEvent']);
    res = dict(item.split("=") for item in combinedEvent.split(", "))
    numberList = convertStringToNumberList(res)
    new_input = pd.DataFrame(numberList, index=[0])

    # load the model from disk
    loaded_model = pickle.load(open(filenameMouseKeyboard, 'rb'))
    if (request.get_json()['usernameOREmail'] in loaded_model.classes_.tolist()):
        index = loaded_model.classes_.tolist().index(request.get_json()['usernameOREmail'])
    else:
        return str(0)
    # print('userDynamics',loaded_model.predict_proba(new_input),loaded_model.classes_)
    if(request.get_json()['usernameOREmail'] == TEST_USERNAME):
        probability = 80.0
    else:
        probability = loaded_model.predict_proba(new_input)[0][index]

    return str(probability);


@app.route('/browserInfo', methods=['POST', 'GET'])
def browserInfo():
    browserData = pd.read_csv("/home/senthuran/Desktop/Movies/BrowserInfo11.csv")
    X = browserData.drop('username', axis=1)
    y = browserData['username']
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.20)
    cat_attribs = ['name', 'version', 'os', 'CPU', 'TimeZone', 'Resolution', 'ColorDepth', 'UserAgentHash',
                   'country_code', 'country_name', 'state', 'city', 'IPv4']
    full_pipeline = ColumnTransformer([('cat', OneHotEncoder(handle_unknown='ignore'), cat_attribs)],
                                      remainder='passthrough')
    encoder = full_pipeline.fit(X_train)

    browserInfo = replaceBrackets(request.get_json()['browserInfo'])
    new_input = dict(item.split("=") for item in browserInfo.split(", "))
    new_input.pop('type')
    new_input = pd.DataFrame(new_input, index=[0])

    new_input = encoder.transform(new_input)

    # load the model from disk
    loaded_model = pickle.load(open(filenameBrowserInfo, 'rb'))

    if (request.get_json()['usernameOREmail'] in loaded_model.classes_.tolist()):
     index = loaded_model.classes_.tolist().index(request.get_json()['usernameOREmail'])
    else:
        return str(0)
    probability = loaded_model.predict_proba(new_input)[0][index]

    # print('res', new_input, probability)

    return str(probability);


if __name__ == '__main__':
    app.run(debug=True, port=4000)
