--
-- Insert demo user information. To add users to the H2 database, edit things here.
-- 

INSERT INTO users (username, password, enabled) VALUES
  ('user','password',true);


INSERT INTO authorities (username, authority) VALUES
  ('user','ROLE_USER');
    
-- By default, the username column here has to match the username column in the users table, above
INSERT INTO user_info (sub, preferred_username, name, email, email_verified) VALUES
  ('01921.FLANRJQW','user','Demo User','user@example.com', true);