INSERT INTO users(name,password) VALUES ('admin','$2a$10$aaTIgEFfzaespYLQQ00Ur.goy9NmS/XKPi1Haf.NGZHSTEJ59dOUa');

INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_POWERUSER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

INSERT INTO user_roles (user_id, role_id) VALUES (1,1);
INSERT INTO user_roles (user_id, role_id) VALUES (1,2);
INSERT INTO user_roles (user_id, role_id) VALUES (1,3);
