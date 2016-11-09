# OpenID Connect Spring Boot  - Simple UI Sample
---

A sample that does not include the stock MITREid OpenID Connect administration screens and APIs. Only the login, logout and authorize flows are enabled, making this a simple OIDC SSO server only. 

It is intended to show the separation of UI from the OIDC and OAuth 2.0 endpoints, and how to disable undesired features.

The user client approval step is also skipped by adding the client and scopes to client whitelist table. 

After running the app you can simulate a remote client's OIDC authorize redirect flow (username: user, password:password) with the call:

[http://localhost:8080/authorize?response_type=code token id_token&client_id=client&redirect_uri=http://localhost:8080/sampleclient&scope=openid profile email&state=randomstate&nonce=randomnonce](http://localhost:8080/authorize?response_type=code token id_token&client_id=client&redirect_uri=http://localhost:8080/sampleclient&scope=openid profile email&state=randomstate&nonce=randomnonce)

**This configuration is not meant for actual production use.** 

At the very least you should create your own JKWS file, plug into a real database, and have method of managing users and client definitions etc. 



