import sys, csv, math
import matplotlib.pyplot as plt

with open('dataSamples/extension/mixedResult.csv', 'r') as openfile:
    reader = csv.reader(openfile)
    data = list(reader)
data = data[1:]
x = []
y = []
for i in range(len(data)):
    x.append(float(data[i][0]))
    y.append(float(data[i][1]))
plt.scatter(x, y, color="red", alpha=0.5, s=20)
plt.show()