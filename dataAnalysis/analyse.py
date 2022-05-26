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

def getQuietMean(data):
    result = 0
    for i in range(1, len(data)):
        result += int(data[i][1])
    return result/totalTime

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

def getAmplitude(data):
    localMaxList = []
    localMinList = []
    for i in range(2, len(data)-1):
        if (int(data[i][1]) > int(data[i-1][1])) and (int(data[i][1]) > int(data[i+1][1])):
            localMaxList.append(i)
        if (int(data[i][1]) < int(data[i-1][1])) and (int(data[i][1]) < int(data[i+1][1])):
            localMinList.append(i)
    totalPeriodNum = min(len(localMaxList), len(localMinList))-1
    if (len(localMaxList)!=0) and (len(localMinList)!=0):
        maxAmp = max(localMaxList)
        minAmp = min(localMinList)
        return (maxAmp-minAmp)/totalPeriodNum
    else:
        return 0.0

def kl_divergence(p, q, agent_density):
    result = 0
    for i in range(len(q)):
        result += p[i]/(mapSize*agent_density) * math.log(p[i]/q[i])
    return result

def mySort(e):
    return e[0]

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
        avgSlope += abs(((pData[i+1][1]-pData[i][1])- (qData[i+1][1]-qData[i][1]))/(qData[i+1][1]-qData[i][1]))
    return round(avgSlope/len(p),4)

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
        result.append(str(params[i][0])+'_'+str(params[i][1])+'_'+str(float(params[i][2]))+'_'+str(params[i][3])+'_'+str(params[i][4])+'_')
    return result

def compareAllAspect(testMode):
    prefixList = filePrefixGenerator(testMode)
    netlogoQuietMeans = []
    netlogoPeriodMeans = []
    netlogoAmplitudeMeans = []
    javaQuietMeans = []
    javaPeriodMeans = []
    javaAmplitudeMeans = []
    for s in range(sampleSize):
        nQMean = 0
        nPMean = 0
        nAMean = 0
        jQMean = 0
        jPMean = 0
        jAMean = 0
        for i in range(repetition):
            with open('dataSamples/netlogo/'+prefixList[s]+str(i)+'.csv', 'r') as openfile:
                reader = csv.reader(openfile)
                data = list(reader)
            nQMean += getQuietMean(data)
            nPMean += getPeriodMean(data)
            nAMean += getAmplitude(data)
        for i in range(repetition):
            with open('dataSamples/java/'+prefixList[s]+str(i)+'.csv', 'r') as openfile:
                reader = csv.reader(openfile)
                data = list(reader)
            jQMean += getQuietMean(data)
            jPMean += getPeriodMean(data)
            jAMean += getAmplitude(data)
        netlogoQuietMeans.append(nQMean/repetition)
        netlogoPeriodMeans.append(nPMean/repetition)
        netlogoAmplitudeMeans.append(nAMean/repetition)
        javaQuietMeans.append(jQMean/repetition)
        javaPeriodMeans.append(jPMean/repetition)
        javaAmplitudeMeans.append(jAMean/repetition)
    return {'nQM': netlogoQuietMeans,
            'nPM': netlogoPeriodMeans,
            'nAM': netlogoAmplitudeMeans,
            'jQM': javaQuietMeans,
            'jPM': javaPeriodMeans,
            'jAM': javaAmplitudeMeans}

if (testMode == 'default'):
    netlogoQuietMeans = []
    javaQuietMeans = []
    netlogoPeriodMeans = []
    javaPeriodMeans = []
    netlogoAmplitudeMeans = []
    javaAmplitudeMeans = []
    for i in range(repetition):
        with open('dataSamples/netlogo/0.04_0.7_7.0_0.82_30_'+str(i)+'.csv', 'r') as openfile:
            reader = csv.reader(openfile)
            data = list(reader)
        netlogoQuietMeans.append(getQuietMean(data))
        netlogoPeriodMeans.append(getPeriodMean(data))
        netlogoAmplitudeMeans.append(getAmplitude(data))
        with open('dataSamples/java/0.04_0.7_7.0_0.82_30_'+str(i)+'.csv', 'r') as openfile:
            reader = csv.reader(openfile)
            data = list(reader)
        javaQuietMeans.append(getQuietMean(data))
        javaPeriodMeans.append(getPeriodMean(data))
        javaAmplitudeMeans.append(getAmplitude(data))
    netlogoQuietMeans.sort()
    javaQuietMeans.sort()
    netlogoPeriodMeans.sort()
    javaPeriodMeans.sort()
    netlogoAmplitudeMeans.sort()
    javaAmplitudeMeans.sort()

    plt.figure(figsize=(12,4))
    plt.subplot(1, 3, 1)
    plt.hist(netlogoQuietMeans, 10, facecolor='b', alpha=0.5, label='netlogo')
    plt.hist(javaQuietMeans, 10, facecolor='r', alpha=0.5, label='java')
    plt.title('Quiet Mean Distribution')
    plt.xlim(770, 880)
    plt.text(780, 10, 'KL divergence = ' + str(round(kl_divergence(javaQuietMeans, netlogoQuietMeans, 0.7), 4)))
    plt.legend()

    plt.subplot(1, 3, 2)
    plt.hist(netlogoPeriodMeans, 10, facecolor='b', alpha=0.5, label='netlogo')
    plt.hist(javaPeriodMeans, 10, facecolor='r', alpha=0.5, label='java')
    plt.title('Period Mean Distribution')
    plt.text(14, 10, 'KL divergence = ' + str(round(kl_divergence(javaPeriodMeans, netlogoPeriodMeans, 0.7), 4)))

    plt.subplot(1, 3, 3)
    plt.hist(netlogoAmplitudeMeans, 10, facecolor='b', alpha=0.5, label='netlogo')
    plt.hist(javaAmplitudeMeans, 10, facecolor='r', alpha=0.5, label='java')
    plt.title('Amplitude Mean Distribution')
    plt.text(14, 10, 'KL divergence = ' + str(round(kl_divergence(javaAmplitudeMeans, netlogoAmplitudeMeans, 0.7), 4)))

    plt.show()

elif (testMode != "None"):
    prefixList = filePrefixGenerator(testMode)
    netlogoQuietMeans = []
    netlogoPeriodMeans = []
    netlogoAmplitudeMeans = []
    javaQuietMeans = []
    javaPeriodMeans = []
    javaAmplitudeMeans = []
    for s in range(sampleSize):
        nQMean = 0
        nPMean = 0
        nAMean = 0
        jQMean = 0
        jPMean = 0
        jAMean = 0
        for i in range(repetition):
            with open('dataSamples/netlogo/'+prefixList[s]+str(i)+'.csv', 'r') as openfile:
                reader = csv.reader(openfile)
                data = list(reader)
            nQMean += getQuietMean(data)
            nPMean += getPeriodMean(data)
            nAMean += getAmplitude(data)
        for i in range(repetition):
            with open('dataSamples/java/'+prefixList[s]+str(i)+'.csv', 'r') as openfile:
                reader = csv.reader(openfile)
                data = list(reader)
            jQMean += getQuietMean(data)
            jPMean += getPeriodMean(data)
            jAMean += getAmplitude(data)
        netlogoQuietMeans.append(nQMean/repetition)
        netlogoPeriodMeans.append(nPMean/repetition)
        netlogoAmplitudeMeans.append(nAMean/repetition)
        javaQuietMeans.append(jQMean/repetition)
        javaPeriodMeans.append(jPMean/repetition)
        javaAmplitudeMeans.append(jAMean/repetition)

    plt.figure(figsize=(12,4))
    plt.subplot(1, 3, 1)
    plt.scatter(testParams[testMode], javaQuietMeans, color='red', alpha=0.5)
    plt.scatter(testParams[testMode], netlogoQuietMeans, color='blue', alpha=0.5)
    plt.text(testParams[testMode][1], max(netlogoQuietMeans), 'Dissimilarity = '+str(getDissimilarity(javaQuietMeans, netlogoQuietMeans, testMode)))
    plt.title('Quiet Mean Vs '+variableDict[testMode])
    plt.subplot(1, 3, 2)
    plt.scatter(testParams[testMode], javaPeriodMeans, color='red', alpha=0.5)
    plt.scatter(testParams[testMode], netlogoPeriodMeans, color='blue', alpha=0.5)
    plt.text(testParams[testMode][1], max(netlogoPeriodMeans), 'Dissimilarity = '+str(getDissimilarity(javaPeriodMeans, netlogoPeriodMeans, testMode)))
    plt.title('Period Mean Vs '+variableDict[testMode])
    plt.subplot(1, 3, 3)
    plt.scatter(testParams[testMode], javaAmplitudeMeans, color='red', alpha=0.5)
    plt.scatter(testParams[testMode], netlogoAmplitudeMeans, color='blue', alpha=0.5)
    plt.text(testParams[testMode][1], max(netlogoAmplitudeMeans), 'Dissimilarity = '+str(getDissimilarity(javaAmplitudeMeans, netlogoAmplitudeMeans, testMode)))
    plt.title('Amplitude Mean Vs '+variableDict[testMode])

    plt.show()
else:
    cop = compareAllAspect('cop')
    agent = compareAllAspect('agent')
    vision = compareAllAspect('vision')
    legit = compareAllAspect('legit')
    myMJT = compareAllAspect('MJT')
    plt.figure(figsize=(10,7.6))

    plt.subplot(5, 3, 1)
    plt.scatter(testParams['cop'], cop['jQM'], color='red', alpha=0.5)
    plt.scatter(testParams['cop'], cop['nQM'], color='blue', alpha=0.5)
    plt.subplot(5, 3, 2)
    plt.scatter(testParams['cop'], cop['jPM'], color='red', alpha=0.5)
    plt.scatter(testParams['cop'], cop['nPM'], color='blue', alpha=0.5)
    plt.subplot(5, 3, 3)
    plt.scatter(testParams['cop'], cop['jAM'], color='red', alpha=0.5)
    plt.scatter(testParams['cop'], cop['nAM'], color='blue', alpha=0.5)

    plt.subplot(5, 3, 4)
    plt.scatter(testParams['agent'], agent['jQM'], color='red', alpha=0.5)
    plt.scatter(testParams['agent'], agent['nQM'], color='blue', alpha=0.5)
    plt.subplot(5, 3, 5)
    plt.scatter(testParams['agent'], agent['jPM'], color='red', alpha=0.5)
    plt.scatter(testParams['agent'], agent['nPM'], color='blue', alpha=0.5)
    plt.subplot(5, 3, 6)
    plt.scatter(testParams['agent'], agent['jAM'], color='red', alpha=0.5)
    plt.scatter(testParams['agent'], agent['nAM'], color='blue', alpha=0.5)

    plt.subplot(5, 3, 7)
    plt.scatter(testParams['vision'], vision['jQM'], color='red', alpha=0.5)
    plt.scatter(testParams['vision'], vision['nQM'], color='blue', alpha=0.5)
    plt.subplot(5, 3, 8)
    plt.scatter(testParams['vision'], vision['jPM'], color='red', alpha=0.5)
    plt.scatter(testParams['vision'], vision['nPM'], color='blue', alpha=0.5)
    plt.subplot(5, 3, 9)
    plt.scatter(testParams['vision'], vision['jAM'], color='red', alpha=0.5)
    plt.scatter(testParams['vision'], vision['nAM'], color='blue', alpha=0.5)

    plt.subplot(5, 3, 10)
    plt.scatter(testParams['legit'], legit['jQM'], color='red', alpha=0.5)
    plt.scatter(testParams['legit'], legit['nQM'], color='blue', alpha=0.5)
    plt.subplot(5, 3, 11)
    plt.scatter(testParams['legit'], legit['jPM'], color='red', alpha=0.5)
    plt.scatter(testParams['legit'], legit['nPM'], color='blue', alpha=0.5)
    plt.subplot(5, 3, 12)
    plt.scatter(testParams['legit'], legit['jAM'], color='red', alpha=0.5)
    plt.scatter(testParams['legit'], legit['nAM'], color='blue', alpha=0.5)

    plt.subplot(5, 3, 13)
    plt.scatter(testParams['MJT'], myMJT['jQM'], color='red', alpha=0.5)
    plt.scatter(testParams['MJT'], myMJT['nQM'], color='blue', alpha=0.5)
    plt.subplot(5, 3, 14)
    plt.scatter(testParams['MJT'], myMJT['jPM'], color='red', alpha=0.5)
    plt.scatter(testParams['MJT'], myMJT['nPM'], color='blue', alpha=0.5)
    plt.subplot(5, 3, 15)
    plt.scatter(testParams['MJT'], myMJT['jAM'], color='red', alpha=0.5)
    plt.scatter(testParams['MJT'], myMJT['nAM'], color='blue', alpha=0.5)

    plt.show()
            