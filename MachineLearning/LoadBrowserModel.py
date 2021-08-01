import pandas as pd
import pickle
from sklearn.compose import ColumnTransformer
from sklearn.preprocessing import OneHotEncoder
from sklearn.model_selection import train_test_split
from sklearn.svm import SVC

filename = 'BrowserInfo.sav'

new_input= pd.DataFrame({"name":['chrome'],"version": ['85.0.4183'],"os" :['Linux']	,"CPU" : ['amd64'],"TimeZone" :['India Standard Time'],"Resolution": ['1920x1053'],"ColorDepth" :['24'],"UserAgentHash" :['2343589699'],"country_code":['LK'],"country_name" :['Sri Lanka'],"state": ['Colombo District'],"city" :['Colombo'],"IPv4" :['112.134.83.212']})

keyboard = pd.read_csv("/home/senthuran/Desktop/Movies/BrowserInfo11.csv")
print(keyboard.shape)
print(keyboard.head())
X = keyboard.drop('username', axis=1)
y = keyboard['username']

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = 0.20)

cat_attribs = ['name', 'version', 'os', 'CPU','TimeZone','Resolution','ColorDepth','UserAgentHash','country_code','country_name','state','city','IPv4']
full_pipeline = ColumnTransformer([('cat', OneHotEncoder(handle_unknown='ignore'), cat_attribs)], remainder='passthrough')

print('X_test',new_input,X_test)
print('shape',new_input.shape)

encoder = full_pipeline.fit(X_train)

new_input = encoder.transform(new_input)

loaded_model = pickle.load(open(filename, 'rb'))

print(loaded_model.predict(new_input))







