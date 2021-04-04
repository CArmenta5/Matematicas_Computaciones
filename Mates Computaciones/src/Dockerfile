FROM openjdk:8
Copy . src/java
WORKDIR src/java
#RUN ["javac", "Reemplazador.java"]
#RUN ["javac", "PanelPrincipal.java"]
#RUN ["javac", "Gui.java"]
#ENTRYPOINT ["java", "Gui"]

RUN ["javac", "ReemplazadorCon.java"]
ENTRYPOINT ["java", "ReemplazadorCon"]