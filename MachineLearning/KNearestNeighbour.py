import pandas as pd
from sklearn.multiclass import OneVsRestClassifier
from xgboost import XGBClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, precision_score,recall_score,f1_score
from sklearn.metrics import classification_report, confusion_matrix
import pickle
from sklearn.neighbors import KNeighborsClassifier

# from sklearn.svm import SVC

keyboard = pd.read_csv("/home/senthuran/Desktop/Movies/MouseKey.csv")
print(keyboard.shape)
print(keyboard.head())
X = keyboard.drop('username', axis=1)
y = keyboard['username']

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = 0.20)

model = OneVsRestClassifier(KNeighborsClassifier())
model.fit(X_train, y_train)

# save the model to disk
filename = 'MouseKey1.sav'
pickle.dump(model, open(filename, 'wb'))

new_input= pd.DataFrame({"maxPositiveAcc":[7247], "maxNegativeAcc":[-8857], "maxSpeed":[886], "totalX":[1029], "totalY":[642], "total":[1340.513905],"leftClick": [6], "rightClick":[0], "mouseDown":[3], "mouseUp":[3], "usernameWPS":[0.003121452894], "passwordWPS":[2.63E-03], "totalTimeSpent":[7715], "countShift":[1],"countCapslock":[0],"countKey":[23], "dwellTimeAverage":[79.73913043], "flightTimesAverage":[348], "upDownTimeAverage":[205.9047619]})

print(X_test)
y_pred = model.predict(X_test)

y_pred_prob = model.predict_proba(X_test)
print(y_pred)
print(y_pred_prob)

print(confusion_matrix(y_test,y_pred))
print(classification_report(y_test,y_pred))

print(accuracy_score(y_test,y_pred)*100)

print(model.predict_proba(new_input))

# precision = precision_score(y_test, y_pred, average='binary')
# recall = recall_score(y_test, y_pred, average='binary')
# score = f1_score(y_test, y_pred, average='binary')
#
# print('Recall: %.3f' % recall)
# print('F-Measure: %.3f' % score)



