FROM openjdk:17-jdk
WORKDIR /app
EXPOSE 8080
ENV jar calculation-api-1.0.0-SNAPSHOT.jar
COPY target/${jar} /app/

CMD java \
    # 启用容器支持，让JVM能够正确识别容器的资源限制
    -XX:+UseContainerSupport \
    # 设置最大堆内存为容器内存限制的70%
    -XX:MaxRAMPercentage=70.0 \
    # 使用G1垃圾收集器
    -XX:+UseG1GC \
    # 设置G1收集器的停顿时间目标为200ms
    -XX:MaxGCPauseMillis=200 \
    # 启用JVM崩溃日志
    -XX:+HeapDumpOnOutOfMemoryError \
    -XX:HeapDumpPath=/app/heap-dump.hprof \
    # 设置时区
    -Duser.timezone=Asia/Shanghai \
    # 启用详细GC日志
    -Xlog:gc*=info:file=/app/gc.log:time,uptime,level,tags:filecount=5,filesize=20M \
    -jar ${jar}