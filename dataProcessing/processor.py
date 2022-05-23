import csv
import sys

infoStart = 7
infoEnd = 14
dataStart = 22

GOV = 0
AGT = 2
VIS = 3
MJT = 4
COP = 6
# prefixIndex = sys.argv.index('-n')
# filePrefix = sys.argv[prefixIndex + 1]

inputIndex = sys.argv.index('-i')
inputFile = sys.argv[inputIndex + 1]




with open(inputFile, 'r') as openfile:
    reader = csv.reader(openfile)
    data = list(reader)
# data = data[dataStart:]
runNumber = int(data[6][-1])
# print(data[infoStart:infoEnd])

def writeOutput(data, runNumber):
    jobInfo = data[infoStart:infoEnd]
    data = data[dataStart:]

    fields = ['t', 'q', 'j', 'a']
    result = []
    for r in range(runNumber):
        oneResult = []
        oneResult.append(fields)
        result.append(oneResult)
    for i in range(len(data)):
        for r in range(runNumber):
            row = []
            row.append(i)
            row.append(data[i][r*3+1])
            row.append(data[i][r*3+2])
            row.append(data[i][r*3+3])
            result[r].append(row)
    for r in range(runNumber):
        outName = 'dataSamples/netlogo/'+str(float(jobInfo[COP][r*3+1])/100)+'_'+str(float(jobInfo[AGT][r*3+1])/100)+'_'+str(float(jobInfo[VIS][r*3+1]))+'_'+jobInfo[GOV][r*3+1]+'_'+jobInfo[MJT][r*3+1]+'_'+ str(r+1) + '.csv'
        with open(outName, 'w') as f:
            write = csv.writer(f)
            write.writerows(result[r])

writeOutput(data, runNumber)
