# 1. OpenJDK 17 기반 이미지 사용
FROM eclipse-temurin:17-jdk-alpine

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. JAR 파일 복사 (실제 JAR 이름에 맞게 수정)
COPY build/libs/*.jar app.jar

# 4. 실행 명령어 설정
ENTRYPOINT ["java", "-jar", "app.jar"]