import pandas as pd
from sklearn.multiclass import OneVsRestClassifier
from xgboost import XGBClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, mean_squared_error
from sklearn.metrics import classification_report, confusion_matrix
from sklearn.compose import ColumnTransformer
from sklearn.preprocessing import OneHotEncoder

keyboard = pd.read_csv("/home/senthuran/Desktop/Movies/BrowserInfo.csv")
print(keyboard.shape)
print(keyboard.head())
X = keyboard.drop('username', axis=1)
y = keyboard['username']

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = 0.20)

cat_attribs = ['name', 'version', 'os', 'CPU','TimeZone','Resolution','UserAgent1','UserAgent2','country_code','country_name','state','city','IPv4']
full_pipeline = ColumnTransformer([('cat', OneHotEncoder(handle_unknown='ignore'), cat_attribs)], remainder='passthrough')

encoder = full_pipeline.fit(X_train)
X_train = encoder.transform(X_train)
X_test = encoder.transform(X_test)

model = OneVsRestClassifier(XGBClassifier())
model.fit(X_train, y_train)
print("hello ",X_test)
print(X_test)
y_pred = model.predict(X_test)

y_pred_prob = model.predict_proba(X_test)
print(y_pred)
print(y_pred_prob)
print("HEllo",model.classes_)

print(confusion_matrix(y_test,y_pred))
print(classification_report(y_test,y_pred))

print(accuracy_score(y_test,y_pred)*100)






