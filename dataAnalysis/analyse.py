import sys, csv
import numpy as np
import matplotlib.pyplot as plt

cop_test = {"cop":[0.01, 0.04, 0.02, 0.06, 0.08, 0.1],
            "agent":0.7,
            "vision":7,
            "legit":0.82,
            "max_jail_term":30}

agent_test = {"cop":0.04,
            "agent":[0.4, 0.5, 0.6, 0.7, 0.8, 0.9],
            "vision":7,
            "legit":0.82,
            "max_jail_term":30}

vision_test = {"cop":0.04,
            "agent":0.7,
            "vision":[2, 4, 6, 7, 8, 10],
            "legit":0.82,
            "max_jail_term":30}

legit_test = {"cop":0.04,
            "agent":0.7,
            "vision":7,
            "legit":[0.2, 0.4, 0.6, 0.8, 0.82, 1.0],
            "max_jail_term":30}

MJT_test = {"cop":0.04,
            "agent":0.7,
            "vision":7,
            "legit":0.82,
            "max_jail_term":[0, 10, 20, 30, 40, 50]}

default_test = {"cop":0.04,
            "agent":0.7,
            "vision":7,
            "legit":0.82,
            "max_jail_term":30}

repetition = 50

inputIndex = sys.argv.index('-i')
testMode = sys.argv[inputIndex + 1]

def getQuietMean(data):
    totalTime = 1000
    result = 0
    for i in range(1, len(data)):
        result += int(data[i][1])
    return result/1000

def getPeriodMean(data):
    result = 0
    localMaxList = []
    for i in range(2, len(data)-1):
        if (int(data[i][1]) > int(data[i-1][1])) and (int(data[i][1]) > int(data[i+1][1])):
            localMaxList.append(i)
    totalPeriodNum = len(localMaxList)-1
    for i in range(len(localMaxList)-1):
        result += localMaxList[i+1]-localMaxList[i]
    return result/totalPeriodNum
    


with open('dataSamples/netlogo/0.04_0.7_7.0_0.82_30_0.csv', 'r') as openfile:
    reader = csv.reader(openfile)
    data = list(reader)

if (testMode == 'default'):
    quietMeans = []
    # periodMeans = []
    for i in range(repetition):
        with open('dataSamples/netlogo/0.04_0.7_7.0_0.82_30_'+str(i)+'.csv', 'r') as openfile:
            reader = csv.reader(openfile)
            data = list(reader)
        quietMeans.append(getQuietMean(data))
    # qMean = np.array(quietMeans)
    # print(qMean)
    plt.hist(quietMeans, 10, facecolor='g', alpha=0.5, label='netlogo')
    plt.title('Quiet Mean Distribution')
    plt.xlim(770, 880)
    plt.legend()
    plt.show()
