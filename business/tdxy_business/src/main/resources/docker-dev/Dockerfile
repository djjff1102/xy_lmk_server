FROM azul/zulu-openjdk-alpine:8u362
ENV LANG en_US.UTF-8
RUN { \
	echo 'https://mirrors.aliyun.com/alpine/v3.9/main'; \
	echo 'https://mirrors.aliyun.com/alpine/v3.9/community'; \
    } > /etc/apk/repositories \
    && apk add --update ttf-dejavu fontconfig && rm -rf /var/cache/apk/*
ADD tdxy_business.jar /
ENTRYPOINT ["java","-jar","-Duser.timezone=GMT+08","-Dspring.profiles.active=dev","/tdxy_business.jar"]
