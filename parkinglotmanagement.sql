/*create schema parkinglotmanagement;*/
/*location is an alphabet A,B,C*/
/*membership fee 8.99*/
DROP TABLE IF EXISTS parking_lot cascade;
create table parking_lot (
location_ varchar(1),
total_spots integer not null,
open_spots integer,
monthly_membership_fee real,
primary key (location_)
);
/*spot id is just a number and cost of each spot is the same 8.99/hr*/
DROP TABLE IF EXISTS parking_spot cascade;
create table parking_spot(
spot_ID integer not null, 
isVacant boolean,
location_ varchar(1),
time_reservation_starts timestamp,
time_reservation_ends timestamp,/*if isVacant=false*/
reservation_number integer,
primary key (spot_ID,location_),
foreign key (location_) references parking_lot
);

DROP TABLE IF EXISTS reservation cascade;
create table reservation(
order_type varchar(20) not null check (order_type='member' or order_type='drive in' or order_type='online'),
time_start timestamp,
time_end timestamp,
car_plate_number varchar(8),
temp_plate_number varchar(8),
location_ varchar(1),
spot_ID integer,
reservation_number integer,
customer_ID varchar(10),
foreign key (location_) references parking_lot,
foreign key (customer_ID) references Customer,
primary key (reservation_number,customer_ID)
);

DROP TABLE IF EXISTS Customer cascade;
create table Customer(
first_name varchar(20) not null, 
last_name varchar(20) not null, 
customer_ID varchar(10) not null, 
customer_type varchar(10) check (customer_type='member' or customer_type='non-member'), 
credit_card_number varchar(19) not null,
login_time timestamp ,
logout_time timestamp,
primary key(customer_ID)
);

DROP TABLE IF EXISTS Member_ cascade;
create table Member_(
car_plate_number varchar(8) not null,
temp_plate_number varchar(8),
temp_card varchar(19),
customer_ID char(10) not null,
login_username varchar(10) not null,
login_password varchar(20) not null,
primary key (login_username),
foreign key (customer_ID) references Customer
);

DROP TABLE IF EXISTS Non_member cascade;
create table Non_member(
arrival_time timestamp,/*if drive in*/
spot_ID integer,
parking_lot_location varchar(1),
guest_login varchar(10) not null,
customer_ID char(10) not null,
primary key (guest_login),
foreign key (customer_ID) references Customer,
foreign key (parking_lot_location) references parking_lot(location_)
);

DROP TABLE IF EXISTS is_;
create table is_(
customer_ID char(10),
customer_type varchar(10),
login_username varchar(10),
guest_login varchar(10),
primary key (customer_ID,login_username,guest_login,customer_type),
foreign key (customer_type) references Customer,
foreign key (customer_ID) references Customer,
foreign key (login_username) references Member_,
foreign key (guest_login) references Non_member
);

DROP TABLE IF EXISTS employee_ cascade;
create table employee_(
employee_ID varchar(10) not null,
first_name varchar (20),
last_name varchar (20),
SSN varchar (9),
salary varchar(20),
pay_type varchar(10),
employee_type varchar(5) check(employee_type='Staff' or employee_type='Admin'),
employee_password varchar(20) not null,
primary key (employee_ID,employee_password)
);

DROP TABLE IF EXISTS makes_;
CREATE TABLE makes_(
employee_ID varchar(10),
reservation_number integer,
primary key(employee_ID,reservation_number)
);
/*
update after each reservation*/
create view vacantLots as (select location_,open_spots from parking_lot);

create procedure as checkVacantSpots(in timein timestamp, in timeout timestamp, in loc varchar(1),out vacantSpots integer) as
begin
	select spot_ID into vacantSpots
	from parking_spot
	where parking_spot.location_=loc
	MINUS
	select spot_ID
	from parking_spot
	where parking_spot.location_=loc and (time_reservation_starts=timein or time_reservation_ends=timeout)
end;

/*after reservation update isVacant and total spots*/
create trigger reserve after insert on reservation
referencing new row as nrow
for each row 
when (nrow.order_type='member' or nrow.order_type='online' or nrow.order_type='drive in')
begin
	update on parking_lot set open_spots=open_spots-1 where parking_lot.location_=nrow.location_
    update on parking_spot set isVacant=false where parking.spot_ID=nrow.spot_ID and parking.location_=nrow.location_
end;

/*the indexes*/
create index cust_id on Customer(customer_ID);
create index car_plate on Member_(car_plate_number);
create index location_info on parking_lot (location_, open_spots);
create index reservation_number on reservation(reservation_number);

/*the views*/
/* Number of current active reservations*/
create view existing_orders as (select time_reservation_starts,time_reservation_ends,reservation_number from parking_spot where isVacant=false);
/* time log of users*/
create view time_log as (select customer_ID,login_time,logout_time from Customer);
/* enagement of users per parking lot*/
create view member_engagement as(select location_,count(reservation_number) as member_orders from reservation where order_type='member' group by location_); 
create view online_engagement as(select location_,count(reservation_number) as online_orders from reservation where order_type='online' group by location_);
create view drive_in_engagement as(select location_,count(reservation_number) as drive_in_orders from reservation where order_type='drive in' group by location_);
/* percentage of bookings per lot*/
create view parking_usage as(
with member_engagement(location_,member_orders) as(select location_,(count(reservation_number) as member_orders) from reservation where order_type='member' group by location_),
online_engagement(location_,online_orders) as(select location_,(count(reservation_number) as online_orders) from reservation where order_type='online' group by location_),
drive_in_engagement(location_,drive_in_orders) as(select location_,(count(reservation_number) as drive_in_orders) from reservation where order_type='drive in' group by location_),
total_by_location(location_,total_spots) as(select location_,total_spots from parking_lot)
select location_,((member_orders*100/total_spots) as percentageOfMembers),((online_orders*100/total_spots) as percentageOfOnline),((drive_in_orders*100/total_spots) as percentageOfDriveIn) from member_engagement natural join online_engagement natural join drive_in_engagement natural join total_by_location
);
/* monthly revenue from each lot*/
/*select location_,() from parking_lot natural join parking_spot natural join order_ group by location_*/
/* open spots at different days of the week*/
create view monthlyRevenue as (
with nonMemberProfits(month,profitN) as (select month,count(month)*8.99 select month(select time_start from reservation natural join Customer where customer_type=non_member) as month group by month)
driveinProfits(month,profitD) as(select month,count(month)*8.99 select month(select time_start from reservation order_type='drive in')as month group by month)
select month,profitN as nonMemberProfits,profitD as driveinprofits from nonMemberProfits natural join driveinProfits);
/*the roles*/
drop owned by staff;
drop owned by admin_;
drop owned by member_;
drop owned by Non_member;
drop role if exists staff;
drop role if exists admin_;
drop role if exists member_;
drop role if exists Non_member;
create role staff;
create role admin_;
create role member_;
create role Non_member;

/* the authorizations*/
grant insert on reservation to Non_member, member_, staff, admin_;
grant select on reservation to Non_member, member_, staff, admin_;
grant select on Customer, Member_, Non_member to staff,admin_;
grant insert on Member_ to member_;
grant select, delete, update on Customer to admin_;
grant select, delete, insert, update on Non_member, Member_ to staff, admin_;
grant all on existing_orders,member_payments, Non_member_payments, parking_usage, time_log to admin_;
grant all on drive_in_engagement,member_engagement,online_engagement to admin_;
grant insert, delete on existing_orders to staff,admin_;