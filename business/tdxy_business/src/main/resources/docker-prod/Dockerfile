FROM azul/zulu-openjdk-alpine:8u362
ENV LANG en_US.UTF-8
RUN { \
	echo 'https://mirrors.aliyun.com/alpine/v3.9/main'; \
	echo 'https://mirrors.aliyun.com/alpine/v3.9/community'; \
    } > /etc/apk/repositories \
    && apk add --update ttf-dejavu fontconfig && rm -rf /var/cache/apk/*
ADD tdxy_business.jar /
ENV JAVA_OPTS="-Xmx6g -Xms6g"
ENTRYPOINT ["java","-Xms6192m","-Xmx6192m","-jar","-Duser.timezone=GMT+08","-Dspring.profiles.active=prod","/tdxy_business.jar"]
