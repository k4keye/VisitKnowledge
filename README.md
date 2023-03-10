# Visit Knowledge

Knowledge comes to you every morning.

---

## Docker Compose setup
You will need to install Docker and docker-compose.

a Docker Compose setup is provided. It comes with the following :

- mariadb:10.8.3
- redis:7.0.4
- prometheus
- grafana

```
 $ docker-compose up -d
```


## Environment
- JPA
- AOP
- p6spy (v1.5.7)
- validation (v2.5.2)
- SlackBot (v1.27.3)
- spring-batch
- GitHub Actions
- Spring Boot Actuator
- prometheus
- grafana

## batch job
- Operate at 10 a.m. every day
- It performs three tasks.
  1) Synchronize the library(resources/*.library.json) 
  2) Bringing posts from the library
  3) Announces newly added posts.


## Hooks
Provision of commit-msg.sh for git commit convention

Active
```
cp hooks/commit-msg.sh .git/hooks/commit-msg
```


## Usage
```markdown
actuator : http://localhost:8791/actuator  
prometheus : http://localhost:9094
grafana : http://localhost:3000 
```

### Registration
Registration SlackBot
```
POST /knowledge/reg
{
  "token" : "[your bot token]",
  "channel" : "[your bot Channel id]"
}
```

option error alert SlackBot
```
POST /error/reg
{
  "token" : "[your bot token]",
  "channel" : "[your bot Channel id]"
}
```

### redis command
```
> redis-cli 
    > keys * 
    > get [key]
    > flushall 
```