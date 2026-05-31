echo Build Conversion Service
call mvn package -DskipTests -f conversion-service/pom.xml

echo Build History Service
call mvn package -DskipTests -f history-service/pom.xml

echo Build Frontend Service
call mvn package -DskipTests -f frontend-service/pom.xml

@REM echo Launch Docker
@REM docker-compose up --build