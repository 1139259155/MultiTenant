# MultiTenant
Transform micro-service(app) to support multiple tenant 

### Follow these steps:
+ 1.Extend your request header to carry tenant information you request header.
> such as: add a new field('x-tenant-id': 1002) into your rest api

+ 2.Isolate each tenant data in the jvm

> eg: some intermediate state data store in ```threadlocal```,```HashMap```, You need to separate them

+ 3.Isolate all interactions with the middleware

> eg: your application interactions with  ```database```,```redis```,```kafka```


+ 4.Special handling

> eg:
> 1. CronTasks in your app, you need split cronTask by different tenant
> 2. The initialization process of application startup, You need to initialize it for different tenants
