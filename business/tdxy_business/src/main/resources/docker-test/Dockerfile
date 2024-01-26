FROM azul/zulu-openjdk-alpine:8u362
ENV LANG en_US.UTF-8
RUN { \
	echo 'https://mirrors.aliyun.com/alpine/v3.9/main'; \
	echo 'https://mirrors.aliyun.com/alpine/v3.9/community'; \
    } > /etc/apk/repositories \
    && apk add --update ttf-dejavu fontconfig && rm -rf /var/cache/apk/*
ADD tdxy_business.jar /

# 添加JVM参数
ENV JAVA_OPTS="-Xmx4g -Xms4g"

ENTRYPOINT ["java","-Xms4096m","-Xmx4096m","-jar","-Duser.timezone=GMT+08","-Dspring.profiles.active=test","/tdxy_business.jar"]

