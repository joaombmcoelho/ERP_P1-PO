PO_UILIB_DIR=/usr/share/java
PO_CORE_DIR=/usr/share/java
JARNAME=po-bank-app

all:
	(cd src; javac -cp $(PO_UILIB_DIR)/po-uilib.jar:$(PO_CORE_DIR)/po-bank-core.jar -encoding UTF-8 `find . -name \*.java`)
	(cd src; jar cf ../$(JARNAME).jar `find . -name \*.class -o -name \*.java` )

clean:
	$(RM) $(JARNAME).jar `find . -name \*.class`
