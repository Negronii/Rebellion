# Rebellion Simulator

## commandLine Guide 

1. Open the terminal and go to the code folder.
$ cd Rebellion/src
$ javac *.java

2. a. We can choose to run the original model as replicating the NetLogo model or run the extension model.

$ java Main GUI -o  (Run the original model)

$ java Main GUI -e  (Run the extension model)

It will open a GUI window to run the Rebellion model.

2. b.  We can choose to run all parameters set in Main class. For save-path, we have to put it in the runOneFeature method. It will allow the app to save output with pre-defined parameters automatically.

$ java Main Multi -o x y    (It was used to generate data that we used to compare two models) 

$ java Main Multi -e x y   (It was used to generate data that we used to analyze our research question) 

‘x’ means how many times to run.

‘y’ means how many steps to run.

## GUI guide

It is similar to the layout in NetLogo. Below are explanations of turtles:

Green turtle	 -> Quiet agent

Bule turtle	 -> Cop

Red turtle	 -> Active agent

Grey turtle 	 -> Jailed agent

Magenta turtle -> Injured Cop (extension)

Yellow turtle	 -> Injured Agent (extension)


We can set parameters that the Netlogo model contains in the Simulator class.

We can run multiple times to collect the data without GUI displayed.

setup: set up the system and update the graph

go: go one tick and update the graph

run n times: update graph after running n times

save file: the suffix of the file name should be .txt or .csv

Double click on the circle of the graph will give information about all turtles on that grid
