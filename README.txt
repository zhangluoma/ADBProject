a.Teammate:
Name: Huaiyuan Cao UNI:hc2748
Name: Luoma Zhang  UNI:lz2413

b.Files submitted:
BingSearch.java
IRHelper.java
test.java
stopwords.txt
README.txt
commons-codec-1.10.jar
json-20140107.jar

c.How to run:
First Compile: 
javac -cp "./commons-codec-1.10.jar:./json-20140107.jar" BingSearch.java
javac -cp "./commons-codec-1.10.jar:./json-20140107.jar" IRHelper.java
javac -cp "./commons-codec-1.10.jar:./json-20140107.jar" MainEntry.java

Then Run test: 
java -cp .:commons-codec-1.10.jar:json-20140107.jar MainEntry

Then based on the guide, input precision and query, such as:
your precision: 0.9
your key words: Milky Way 

d.A clear description of the internal design of your project
This project contains 5 classes:
1) MainEntry: contains the main method to enter the program, including user input logic and precision checking loop.
2) BingSearch: a static method called “search”, used to search a list of result from Bing with given key words.
3) BingObject: contains the retrieved information.
4) IRHelper: contains several static methods: Method getScore() helps score documents using tf-idf. Method generateNewKey() use rocchio relevance feedback algorithm to pick up the top 2 most relevant words in the given top that doesn’t appear in previous key words.
5) OrderCounter: this class is used to reorder the phrases. for example, if we are going to add two keys, say “york” and “new”, this class helps to order these two words and return “new york”.

e.A detailed description of your query-modification method (this is the core component of the project; see below)
1) Fetch the description and title string, eliminate all leading and trailing space, transfer to lowercase. Then split both the description and title string, concatenate them to a string array. We call this array “vector”.
2) Look up a stop word dictionary, clear those stop words in “vector”
3) Using tf-idf to score every document according to its vector and the given query.
4) Construct a HashMap to store terms and their corresponding weight. a term’s weight is determined by its frequency and the score of the document it comes from. According to Rocchio algorithm, we add the corresponding bucket with a term’s weight if this word comes from the document which we marked relevant, and subtract its weight if not. Here are some tricks such as we increase the weight of the word which is next to the key words. For example, when you search “Columbia”, and if there is “Columbia University” in the result, University has higher weight than other words that are not next to “Columbia”.
5) Scan the HashMap and pick the top 2 most weighted word and reorder them in a right order if necessary, “york new” to “new york” for example, based on their relative position in the origin string. We add the reordered words to the origin keywords, and search again.

f.Our Bing Search Account Key: le5VXjKjrgc7nRivO9KXOSU+vKyaMr4cnFnktYD8dQE

g.Any additional information that you consider significan