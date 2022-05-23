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

with open('dataSamples/netlogo/0.04_0.7_7.0_0.82_30_0.csv', 'r') as openfile:
    reader = csv.reader(openfile)
    data = list(reader)

if (testMode == 'default'):
    quietMeans = []
    print(data[:200])
    # periodMeans = []
