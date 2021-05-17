import pandas as pd
import pickle
from sklearn.model_selection import train_test_split

keyboard = pd.read_csv("/home/senthuran/Desktop/Movies/MouseKey.csv")
print(keyboard.shape)
print(keyboard.head())
X = keyboard.drop('username', axis=1)
y = keyboard['username']

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = 0.20)

filename = 'MouseKey.sav'
# load the model from disk
loaded_model = pickle.load(open(filename, 'rb'))
result = loaded_model.predict(X_test)
print(result)