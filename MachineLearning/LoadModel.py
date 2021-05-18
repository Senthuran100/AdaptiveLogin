import pandas as pd
import pickle

filename = 'MouseKey.sav'
# input data
new_input= pd.DataFrame({"maxPositiveAcc":[7247], "maxNegativeAcc":[-8857], "maxSpeed":[886], "totalX":[1029], "totalY":[642], "total":[1340.513905],"leftClick": [6], "rightClick":[0], "mouseDown":[3], "mouseUp":[3], "usernameWPS":[0.003121452894], "passwordWPS":[2.63E-03], "totalTimeSpent":[7715], "countShift":[1],"countCapslock":[0],"countKey":[23], "dwellTimeAverage":[79.73913043], "flightTimesAverage":[348], "upDownTimeAverage":[205.9047619]})

# load the model from disk
loaded_model = pickle.load(open(filename, 'rb'))

result = loaded_model.predict(new_input)

print(loaded_model.predict_proba(new_input))
print(result)