An Inverted Index (with Positional Information) on the complete works of Shakespeare

Downloading dependencies:

1) org.json from https://repo1.maven.org/maven2/org/json/json/20190722/json-20190722.jar

2) org.apache.commons.cli from http://mirrors.ocf.berkeley.edu/apache//commons/cli/binaries/commons-cli-1.4-bin.zip

3) org.apache.commons.io from http://us.mirrors.quenda.co/apache//commons/io/binaries/commons-io-2.5-bin.zip

All 3 dependencies are also packed into the lib/ directory as well.

Building the project:

I used Eclipse IDE for building and running the project.

Running:

I used the 'Run -> Run Configurations..' option in Eclipse to pass the cmd-line parameters.

Here are some examples:

1) Creating an index:

apps.Indexer <path to JSON file> -i <path to write the index to>

2) Creating an index and writing to disk in compressed format:

apps.Indexer <path to JSON file> -c -i <path to write the index to>

3) Normal query-retrival on 7-term query set of 100 queries:

apps.QueryRetriever -q <path where index can be read from>

4) Run experiment to test compression hypothesis:

  a) run query-retrieval on 7-term query set

    apps.TimingExperiment <path to compressed or uncompressed index on disk> "7"

  b) run query-retrieval on 14-term query set

    apps.TimingExperiment <path to compressed or uncompressed index on disk> "14"

5) Calculate Dice's coefficient for 700-terms:

apps.DiceCoefficientCalculator <path to index on disk>
