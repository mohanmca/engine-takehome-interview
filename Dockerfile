FROM eclipse-temurin:17.0.7_7-jdk
RUN apt-get -y update \
  && apt-get -y install maven \
  && apt-get -y install build-essential \
  && apt-get clean
COPY ./ /app
COPY sample_input.txt /app
RUN cd /app && mvn package
#RUN mvn exec:java -Dexec.mainClass="com.gemini.service.Main" < sample_input.txt
ENTRYPOINT ["/app/execute.sh"]