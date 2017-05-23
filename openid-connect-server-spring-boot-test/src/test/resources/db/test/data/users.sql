--
-- Turn off autocommit and start a transaction so that we can use the temp tables
--

SET AUTOCOMMIT FALSE;

--
-- Insert user information. To add users to the HSQL database, edit things here.
-- 

INSERT INTO users (username, password, enabled) VALUES
  ('admin','password',true),
  ('admin_bcrypt','$2a$10$MzY7AG/8Sf/mbpGz6fFlquD8ymaKsQI5Z0rvjxYP4KeENcrFnr7FO',true),
  ('admin_scrypt', '$e0801$4bV2x7jsYTttcqdCFfHAuWJDFYGJX3Rzi3Ih9PAIWFKsch3o0jtfN4HkJQCxsB+rYKAQr6Lq1BJOG9UastlAiQ==$gVrJsKBQe7vl/F9GEzxLRsYfxEYUukAvj84v3A2x/D8=', true),
  ('admin_standard', '9e2830557e2ac4f60bafddd6e572b37b373b6a09382fef11591927f52fef3fba96a59d2335f09193', true),
  ('admin_pbkdf2', '16d96430514b8fda9ad661bdce180f1d0617c29dd345b03538fa2112e9d3f356b2898b2ea5e00b77', true),
  ('user','password',true);


INSERT INTO authorities (username, authority) VALUES
  ('admin','ROLE_ADMIN'),
  ('admin','ROLE_USER'),
  ('admin_bcrypt','ROLE_ADMIN'),
  ('admin_bcrypt','ROLE_USER'),
  ('admin_scrypt','ROLE_ADMIN'),
  ('admin_scrypt','ROLE_USER'),
  ('admin_standard','ROLE_ADMIN'),
  ('admin_standard','ROLE_USER'),
  ('admin_pbkdf2','ROLE_ADMIN'),
  ('admin_pbkdf2','ROLE_USER'),
  ('user','ROLE_USER');
    
-- By default, the username column here has to match the username column in the users table, above
INSERT INTO user_info (sub, preferred_username, name, email, email_verified) VALUES
  ('90342.ASDFJWFA','admin','Demo Admin','admin@example.com', true),
  ('90343.ASDFJWFA','admin_bcrypt','Demo Admin','admin@example.com', true),
  ('90344.ASDFJWFA','admin_scrypt','Demo Admin','admin@example.com', true),
  ('90345.ASDFJWFA','admin_standard','Demo Admin','admin@example.com', true),
  ('90346.ASDFJWFA','admin_pbkdf2','Demo Admin','admin@example.com', true),
  ('01921.FLANRJQW','user','Demo User','user@example.com', true);
   
-- 
-- Close the transaction and turn autocommit back on
-- 
    
COMMIT;

SET AUTOCOMMIT TRUE;

