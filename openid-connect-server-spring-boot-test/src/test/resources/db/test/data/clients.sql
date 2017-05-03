--
-- Turn off autocommit and start a transaction so that we can use the temp tables
--

SET AUTOCOMMIT FALSE;

--
-- Insert client information 
-- 

INSERT INTO client_details (client_id, client_secret, client_name, dynamically_registered, refresh_token_validity_seconds, access_token_validity_seconds, id_token_validity_seconds, allow_introspection) VALUES
	('client', 'secret', 'Test Client', false, null, 3600, 600, true);
select @a := scope_identity();
	
INSERT INTO client_scope (owner_id, scope) VALUES
	(@a, 'openid'),
	(@a, 'profile'),
	(@a, 'email'),
	(@a, 'address'),
	(@a, 'phone'),
	(@a, 'offline_access');

INSERT INTO client_redirect_uri (owner_id, redirect_uri) VALUES
	(@a, 'http://localhost/'),
	(@a, 'http://localhost:8080/');
	
INSERT INTO client_grant_type (owner_id, grant_type) VALUES
	(@a, 'authorization_code'),
	(@a, 'client_credentials'),
	(@a, 'urn:ietf:params:oauth:grant-type:jwt-bearer'),
	(@a, 'urn:ietf:params:oauth:grant_type:redelegate'),
	(@a, 'implicit'),
	(@a, 'refresh_token');
	

--- Whitelisted client
INSERT INTO client_details (client_id, client_secret, client_name, dynamically_registered, refresh_token_validity_seconds, access_token_validity_seconds, id_token_validity_seconds, allow_introspection) VALUES
	('whitelistclient', 'secret', 'Test Whitelist Client', false, null, 3600, 600, true);
select @a := scope_identity();
	
INSERT INTO client_scope (owner_id, scope) VALUES
	(@a, 'openid'),
	(@a, 'profile'),
	(@a, 'email'),
	(@a, 'address'),
	(@a, 'phone'),
	(@a, 'offline_access');

INSERT INTO client_redirect_uri (owner_id, redirect_uri) VALUES
	(@a, 'http://whitelist.localhost/'),
	(@a, 'http://whitelist.localhost:8090/');
	
INSERT INTO client_grant_type (owner_id, grant_type) VALUES
	(@a, 'authorization_code'),
	(@a, 'client_credentials'),
	(@a, 'urn:ietf:params:oauth:grant-type:jwt-bearer'),
	(@a, 'urn:ietf:params:oauth:grant_type:redelegate'),
	(@a, 'implicit'),
	(@a, 'refresh_token');
	
INSERT INTO whitelisted_site(client_id, creator_user_id) VALUES
	('whitelistclient','unit-tests');
	
select @whitelisted_site_id := scope_identity();

INSERT INTO whitelisted_site_scope(owner_id, scope) VALUES
	(@whitelisted_site_id, 'openid'),
	(@whitelisted_site_id, 'profile'),
	(@whitelisted_site_id, 'email'),
	(@whitelisted_site_id, 'address'),
	(@whitelisted_site_id, 'phone'),
	(@whitelisted_site_id, 'offline_access');
	
	
--- password encrypted clients
INSERT INTO client_details (client_id, client_secret, client_name, dynamically_registered, refresh_token_validity_seconds, access_token_validity_seconds, id_token_validity_seconds, allow_introspection) VALUES
	('client_bcrypt', '$2a$10$fQtoIZ24X.MRlFuW0DUbIekejcZ.xwfOsL616EUvMAdIEWMAFBtgS', 'Test BCrypt Client', false, null, 3600, 600, true);
select @a := scope_identity();
	
INSERT INTO client_scope (owner_id, scope) VALUES
	(@a, 'openid'),
	(@a, 'profile'),
	(@a, 'email'),
	(@a, 'address'),
	(@a, 'phone'),
	(@a, 'offline_access');

INSERT INTO client_redirect_uri (owner_id, redirect_uri) VALUES
	(@a, 'http://localhost/'),
	(@a, 'http://localhost:8090/');
	
INSERT INTO client_grant_type (owner_id, grant_type) VALUES
	(@a, 'authorization_code'),
	(@a, 'client_credentials'),
	(@a, 'urn:ietf:params:oauth:grant-type:jwt-bearer'),
	(@a, 'urn:ietf:params:oauth:grant_type:redelegate'),
	(@a, 'implicit'),
	(@a, 'refresh_token');
	
INSERT INTO client_details (client_id, client_secret, client_name, dynamically_registered, refresh_token_validity_seconds, access_token_validity_seconds, id_token_validity_seconds, allow_introspection) VALUES
	('client_scrypt', '$e0801$uktbg1b714mXwczFiu5+zTgVUf4/a5neBj2on0VAi4F/3gf8TLAuXvusB3uqt4fDd+AoPO4JH9C0DeVVI1fmng==$xOPv9ER0IxZLgvYVJsCX+CPWEpb8CLBES9pOLpi1d00=', 'Test SCrypt Client', false, null, 3600, 600, true);
select @a := scope_identity();
	
INSERT INTO client_scope (owner_id, scope) VALUES
	(@a, 'openid'),
	(@a, 'profile'),
	(@a, 'email'),
	(@a, 'address'),
	(@a, 'phone'),
	(@a, 'offline_access');

INSERT INTO client_redirect_uri (owner_id, redirect_uri) VALUES
	(@a, 'http://localhost/'),
	(@a, 'http://localhost:8090/');
	
INSERT INTO client_grant_type (owner_id, grant_type) VALUES
	(@a, 'authorization_code'),
	(@a, 'client_credentials'),
	(@a, 'urn:ietf:params:oauth:grant-type:jwt-bearer'),
	(@a, 'urn:ietf:params:oauth:grant_type:redelegate'),
	(@a, 'implicit'),
	(@a, 'refresh_token');	
	
	
INSERT INTO client_details (client_id, client_secret, client_name, dynamically_registered, refresh_token_validity_seconds, access_token_validity_seconds, id_token_validity_seconds, allow_introspection) VALUES
	('client_standard', 'f1651aa441dc41ebfb233ff47e9087c76c0c8325a4764b1de94f380956c4e02139c92a52ff2f26f1', 'Test Standard Client', false, null, 3600, 600, true);
select @a := scope_identity();
	
INSERT INTO client_scope (owner_id, scope) VALUES
	(@a, 'openid'),
	(@a, 'profile'),
	(@a, 'email'),
	(@a, 'address'),
	(@a, 'phone'),
	(@a, 'offline_access');

INSERT INTO client_redirect_uri (owner_id, redirect_uri) VALUES
	(@a, 'http://localhost/'),
	(@a, 'http://localhost:8090/');
	
INSERT INTO client_grant_type (owner_id, grant_type) VALUES
	(@a, 'authorization_code'),
	(@a, 'client_credentials'),
	(@a, 'urn:ietf:params:oauth:grant-type:jwt-bearer'),
	(@a, 'urn:ietf:params:oauth:grant_type:redelegate'),
	(@a, 'implicit'),
	(@a, 'refresh_token');		
	
INSERT INTO client_details (client_id, client_secret, client_name, dynamically_registered, refresh_token_validity_seconds, access_token_validity_seconds, id_token_validity_seconds, allow_introspection) VALUES
	('client_pbkdf2', '81855f71c0d5ae7f39efe2740856e78d2c14c4ba4ccd1ef29ab5423cec871c99d8ac3c9a8037d41c', 'Test Standard Client', false, null, 3600, 600, true);
select @a := scope_identity();
	
INSERT INTO client_scope (owner_id, scope) VALUES
	(@a, 'openid'),
	(@a, 'profile'),
	(@a, 'email'),
	(@a, 'address'),
	(@a, 'phone'),
	(@a, 'offline_access');

INSERT INTO client_redirect_uri (owner_id, redirect_uri) VALUES
	(@a, 'http://localhost/'),
	(@a, 'http://localhost:8090/');
	
INSERT INTO client_grant_type (owner_id, grant_type) VALUES
	(@a, 'authorization_code'),
	(@a, 'client_credentials'),
	(@a, 'urn:ietf:params:oauth:grant-type:jwt-bearer'),
	(@a, 'urn:ietf:params:oauth:grant_type:redelegate'),
	(@a, 'implicit'),
	(@a, 'refresh_token');		
-- 
-- Close the transaction and turn autocommit back on
-- 
    
COMMIT;

SET AUTOCOMMIT TRUE;
	