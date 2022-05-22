import csv
import sys


dataStart = 22

prefixIndex = sys.argv.index('-n')
filePrefix = sys.argv[prefixIndex + 1]

inputIndex = sys.argv.index('-i')
inputFile = sys.argv[inputIndex + 1]




with open(inputFile, 'r') as openfile:
    reader = csv.reader(openfile)
    data = list(reader)
data = data[dataStart:]
repetition = len(data[0])//3

def writeOutput(data, repetition, outPrefix):
    fields = ['t', 'q', 'j', 'a']
    result = []
    for r in range(repetition):
        oneResult = []
        oneResult.append(fields)
        result.append(oneResult)
    for i in range(len(data)):
        for r in range(repetition):
            row = []
            row.append(i)
            row.append(data[i][r*3+1])
            row.append(data[i][r*3+2])
            row.append(data[i][r*3+3])
            result[r].append(row)
    for r in range(repetition):
        outName = 'processedData/' + outPrefix + 'NetLogo' + str(r) + '.csv'
        with open(outName, 'w') as f:
            write = csv.writer(f)
            write.writerows(result[r])

writeOutput(data, repetition, filePrefix)
