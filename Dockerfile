FROM eclipse-temurin:17.0.7_7-jdk
RUN apt-get -y update \
  && apt-get -y install maven \
  && apt-get -y install build-essential \
  && apt-get clean
COPY . /app
RUN cp /app/bin/*.sh /app/
RUN mvn -f /app/pom.xml package -Dmaven.test.skip=true
ENTRYPOINT ["/app/execute_test.sh"]