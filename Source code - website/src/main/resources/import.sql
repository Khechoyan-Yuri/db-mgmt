-- Insert role
insert into role (name) values ('ROLE_ADMIN');

-- Insert users
insert into user (username,enabled,password,role_id) values ('admin',true,'',1);

-- Insert customers
insert into customer (firstname,lastname,mobilenumber,reasonforvisit,checkintime,place) values ('Vikram','Hegde','3143152856','billingpayment',CURRENT_TIMESTAMP(),0)
insert into customer (firstname,lastname,mobilenumber,reasonforvisit,checkintime,place) values ('Yuri','Khechoyan','3143152856','techsupport',CURRENT_TIMESTAMP(),0)
insert into customer (firstname,lastname,mobilenumber,reasonforvisit,checkintime,place) values ('Zahid','Anwar','3143152856','acctissues',CURRENT_TIMESTAMP(),0)