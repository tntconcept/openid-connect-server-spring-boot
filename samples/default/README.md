# OpenID Connect Spring Boot  - Default Sample
---

A Spring Boot Application configured to mimic the default [MITREid OpenID Connect WebApp](https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server/tree/master/openid-connect-server-webapp) meant for maven and gradle overlay projects.

After running the app you can simulate a remote client's OIDC authorize redirect flow (username: user, password:password) with the call:

[http://localhost:8080/authorize?response_type=code token id_token&client_id=client&redirect_uri=http://localhost:8080/&scope=openid profile email&state=randomstate&nonce=randomnonce](http://localhost:8080/authorize?response_type=code token id_token&client_id=client&redirect_uri=http://localhost:8080/&scope=openid profile email&state=randomstate&nonce=randomnonce)

