import sys, csv, math
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.pyplot import figure

testParams = {"cop":[0.04, 0.01, 0.02, 0.06, 0.08, 0.1],
            "agent":[0.7, 0.4, 0.5, 0.6, 0.8, 0.9],
            "vision":[7, 2, 4, 6, 8, 10],
            "legit":[0.82, 0.2, 0.4, 0.6, 0.8, 1.0],
            "MJT":[30, 0, 10, 20, 40, 50]}

default_test = {"cop":0.04,
            "agent":0.7,
            "vision":7,
            "legit":0.82,
            "MJT":30}

variableDict = {"cop":"Initial cop density",
            "agent":"Initial agent density",
            "vision":"Vision distance",
            "legit":"Legitimacy",
            "MJT": "Max jail term"}

options = ['cop', 'agent', 'vision', 'legit', 'MJT']
sampleSize = 6
repetition = 50
mapSize = 1600
totalTime = 1000

if '-i' in sys.argv:
    inputIndex = sys.argv.index('-i')
    testMode = sys.argv[inputIndex + 1]
else:
    testMode = 'None'

# Calculate the average Quiet state for a run
def getQuietMean(data):
    result = 0
    for i in range(1, len(data)):
        result += int(data[i][1])
    return result/totalTime

# Calculate the average period of uprising for a run
def getPeriodMean(data):
    result = 0
    localMaxList = []
    for i in range(2, len(data)-1):
        if (int(data[i][1]) > int(data[i-1][1])) and (int(data[i][1]) > 
        int(data[i+1][1])):
            localMaxList.append(i)
    totalPeriodNum = len(localMaxList)-1
    for i in range(len(localMaxList)-1):
        result += localMaxList[i+1]-localMaxList[i]
    return result/totalPeriodNum

# Calculate amplitude of the run
def getAmplitude(data):
    localMaxList = []
    localMinList = []
    for i in range(2, len(data)-1):
        if (int(data[i][1]) > int(data[i-1][1])) and (int(data[i][1]) > 
        int(data[i+1][1])):
            localMaxList.append(i)
        if (int(data[i][1]) < int(data[i-1][1])) and (int(data[i][1]) < 
        int(data[i+1][1])):
            localMinList.append(i)
    totalPeriodNum = min(len(localMaxList), len(localMinList))-1
    if (len(localMaxList)!=0) and (len(localMinList)!=0):
        maxAmp = max(localMaxList)
        minAmp = min(localMinList)
        return (maxAmp-minAmp)/totalPeriodNum
    else:
        return 0.0

# Calculate the KL divergence of a run
def kl_divergence(p, q, agent_density):
    result = 0
    for i in range(len(q)):
        result += p[i]/(mapSize*agent_density) * math.log(p[i]/q[i])
    return result

def mySort(e):
    return e[0]

# Compare the dissimilarity based on how dependent variable changes 
# based on independent variable
def getDissimilarity(p, q, testMode):
    pData = []
    qData = []
    avgSlope = 0
    for i in range(len(p)):
        pData.append([testParams[testMode][i], p[i]])
        qData.append([testParams[testMode][i], q[i]])
    pData.sort(key=mySort)
    qData.sort(key=mySort)
    for i in range(len(p)-1):
        avgSlope += abs(((pData[i+1][1]-pData[i][1])- 
        (qData[i+1][1]-qData[i][1]))/(qData[i+1][1]-qData[i][1]))
    return round(avgSlope/len(p),4)

# This is for formatting the name of output file 
def filePrefixGenerator(testMode):
    params = []
    result = []
    for i in range(sampleSize):
        oneParamList = []
        for o in range(len(options)):
            if options[o] == testMode:
                oneParamList.append(testParams[testMode][i])
            else:
                oneParamList.append(testParams[options[o]][0])
        params.append(oneParamList)
    for i in range(len(params)):
        result.append(str(params[i][0])+'_'+str(params[i][1])+'_'+
        str(float(params[i][2]))+'_'+str(params[i][3])+'_'+
        str(params[i][4])+'_')
    return result