--
-- Turn off autocommit and start a transaction so that we can use the temp tables
--

SET AUTOCOMMIT FALSE;

--
-- Insert user information. To add users to the HSQL database, edit things here.
-- 

INSERT INTO users (username, password, enabled) VALUES
  ('admin','password',true),
  ('user','password',true);


INSERT INTO authorities (username, authority) VALUES
  ('admin','ROLE_ADMIN'),
  ('admin','ROLE_USER'),
  ('user','ROLE_USER');
    
-- By default, the username column here has to match the username column in the users table, above
INSERT INTO user_info (sub, preferred_username, name, email, email_verified) VALUES
  ('90342.ASDFJWFA','admin','Demo Admin','admin@example.com', true),
  ('01921.FLANRJQW','user','Demo User','user@example.com', true);
   
-- 
-- Close the transaction and turn autocommit back on
-- 
    
COMMIT;

SET AUTOCOMMIT TRUE;

