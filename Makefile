JCC = javac
JFLAGS = -g
default: compile
compile:
	javac -cp "./commons-codec-1.10.jar:./json-20140107.jar" InformationRetrieval.java
