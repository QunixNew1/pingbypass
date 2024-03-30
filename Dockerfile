FROM openjdk:17-alpine

WORKDIR /pingbypass
COPY . /pingbypass

RUN mkdir -p pb-server/run/client/mods
RUN wget -O pb-server/run/client/mods/hmc-specifics-1.20.4.jar \
    https://github.com/3arthqu4ke/hmc-specifics/releases/download/v1.20.4-1.8.1/hmc-specifics-fabric-1.20.4-1.8.1.jar

RUN wget -O pb-server/run/client/mods/hmc-optimizations-0.1.0-fabric.jar \
    https://github.com/3arthqu4ke/hmc-optimizations/releases/download/latest/hmc-optimizations-0.1.0-fabric.jar

RUN mkdir -p pb-server/run/client/
RUN cp -f config/options.txt pb-server/run/client/

RUN mkdir -p pb-server/run/client/pingbypass/server/
RUN cp -f config/pingbypass.properties pb-server/run/client/pingbypass/server/

RUN chmod +x gradlew
RUN ./gradlew -Phmc.lwjgl=true test
RUN ./gradlew -Phmc.lwjgl=true pb-server:fabricJar
RUN ./gradlew -Phmc.lwjgl=true pb-server:fabricPreRunClient
ENTRYPOINT sh -c "./gradlew -Phmc.lwjgl=true pb-server:fabricRunClient --console=plain"
EXPOSE 25565
