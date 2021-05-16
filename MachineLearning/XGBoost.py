import pandas as pd
from sklearn.multiclass import OneVsRestClassifier
from xgboost import XGBClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
from sklearn.metrics import classification_report, confusion_matrix

keyboard = pd.read_csv("/home/senthuran/Desktop/Movies/MouseKey.csv")
print(keyboard.shape)
print(keyboard.head())
X = keyboard.drop('username', axis=1)
y = keyboard['username']

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = 0.20)

model = OneVsRestClassifier(XGBClassifier())
model.fit(X_train, y_train)
print("hello ",X_test)
print(X_test)
y_pred = model.predict(X_test)

y_pred_prob = model.predict_proba(X_test)
print(y_pred)
print(y_pred_prob)

print(confusion_matrix(y_test,y_pred))
print(classification_report(y_test,y_pred))

print(accuracy_score(y_test,y_pred)*100)






