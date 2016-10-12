
--
-- Insert demo client information 
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
	(@a, 'urn:ietf:params:oauth:grant_type:redelegate'),
	(@a, 'implicit'),
	(@a, 'refresh_token');