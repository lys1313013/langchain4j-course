
这里使用的是1.0.0-beta1版本的依赖


demo3 
先使用docker在本地启动weaviate服务
```bash
docker pull semitechnologies/weaviate:latest
```

```bash
docker run -d --name weaviate ^
    --restart=always ^
    -p 38080:8080 ^
    -p 50051:50051 ^
    -e "AUTHENTICATION_APIKEY_ENABLED=true" ^
    -e "AUTHENTICATION_APIKEY_ALLOWED_KEYS=test-secret-key,test2-secret-key" ^
    -e "AUTHENTICATION_APIKEY_USERS=test@2024.com,test2@2024.com" ^
    -e "AUTHORIZATION_ADMINLIST_ENABLED=true" ^
    -e "AUTHORIZATION_ADMINLIST_USERS=test@2024.com" ^
    -e "AUTHORIZATION_ADMINLIST_READONLY_USERS=test2@2024.com" ^
    -e WEAVIATE_HOSTNAME=0.0.0.0 ^
    semitechnologies/weaviate:latest
```