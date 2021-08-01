import pandas as pd
from sklearn.multiclass import OneVsRestClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
from sklearn.metrics import classification_report, confusion_matrix
from sklearn.compose import ColumnTransformer
from sklearn.preprocessing import OneHotEncoder
import pickle
from sklearn.svm import SVC

browserData = pd.read_csv("/home/senthuran/Desktop/BrowserInfo11.csv")
print(browserData.shape)
print(browserData.head())

X = browserData.drop('username', axis=1)
y = browserData['username']

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = 0.20)

cat_attribs = ['name', 'version', 'os', 'CPU','TimeZone','Resolution','ColorDepth','UserAgentHash','country_code','country_name','state','city','IPv4']
full_pipeline = ColumnTransformer([('cat', OneHotEncoder(handle_unknown='ignore'), cat_attribs)], remainder='passthrough')

encoder = full_pipeline.fit(X_train)
X_train = encoder.transform(X_train)
X_test = encoder.transform(X_test)
svclassifier = SVC(kernel='linear',probability=True)

model = OneVsRestClassifier(svclassifier)
model.fit(X_train, y_train)
y_pred = model.predict(X_test)

y_pred_prob = model.predict_proba(X_test)
print(accuracy_score(y_test,y_pred)*100)

# save the model to disk
filename = 'BrowserInfo.sav'
pickle.dump(model, open(filename, 'wb'))

print(y_pred)
print(y_pred_prob)

print(confusion_matrix(y_test,y_pred))
print(classification_report(y_test,y_pred))






