# cookiebot
Keeps track of cookies owed

# Sonar Qube
https://sonarcloud.io/dashboard?id=sawyern.cookiebot%3Acookiebot

# Pre Requisites
Requires java 8+, maven, and database of your choice.

# Configuration
Add a file called "application.yml" to your resources folder.
It should look something like this
```
spring:
  datasource:
    url: jdbc:postgresql://<<hostname>>:5432/<<db name>>
    username: <<your user>>
    password: <<your password>>
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: <<create - to build databases || update - once database tables are created>>
security:
  ignored: /**
discord:
  token: <<discord api bot token>>
```

# Start server
Add the bot to your server https://discordapp.com/oauth2/authorize?client_id=544889972004749312&scope=bot&permissions=0.

navigate to src folder
```mvn spring-boot:run```

