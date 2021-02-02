install:
	mvn compile assembly:single
	sudo cp -f target/minimaschine-3.0-jar-with-dependencies.jar /usr/local/share/java/jars/minimaschine.jar
	sudo cp -f start-skript.sh /usr/local/bin/minimaschine

.phony: install
