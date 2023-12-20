drop database if exists MobileVision;

create database if not exists MobileVision;

use MobileVision;



CREATE TABLE user (
                      UserName VARCHAR(30) NOT NULL PRIMARY KEY,
                      Email VARCHAR(50) NOT NULL,
                      Password VARCHAR(20) NOT NULL
);

CREATE TABLE customer (
                          c_contact VARCHAR(10) NOT NULL PRIMARY KEY,
                          c_name VARCHAR(20) NOT NULL,
                          c_address VARCHAR(20) NOT NULL,
                          c_id VARCHAR(20) NOT NULL

);

CREATE TABLE employee (
                          e_id VARCHAR(20) NOT NULL  PRIMARY KEY,
                          e_name VARCHAR(20) NOT NULL,
                          e_address VARCHAR(20) NOT NULL,
                          e_contact VARCHAR(10) NOT NULL
);


CREATE TABLE attendance (
                            a_id VARCHAR(20) NOT NULL  PRIMARY KEY,
                            e_id VARCHAR(20),
                            month_count VARCHAR (2),
                            FOREIGN KEY(e_id) references employee(e_id) on delete cascade on update cascade
);


CREATE TABLE payments (
                          p_id VARCHAR(20) NOT NULL PRIMARY KEY,
                          c_contact VARCHAR(10),
                          o_id VARCHAR(20),
                          date VARCHAR (10),
                          p_description VARCHAR (20),
                          Amount VARCHAR (10),
                          FOREIGN KEY( c_contact) references customer( c_contact) on delete cascade on update cascade,
                          FOREIGN KEY(o_id) references orders(o_id) on delete cascade on update cascade
);

CREATE TABLE salary(
                       sal_id VARCHAR(20) NOT NULL PRIMARY KEY,
                       e_id VARCHAR(20),
                       sal_month VARCHAR(10),
                       amount VARCHAR (25),
                       FOREIGN KEY(e_id) references employee(e_id) on delete cascade on update cascade
);


CREATE TABLE supplier (
                          sup_contact VARCHAR(10) NOT NULL PRIMARY KEY,
                          sup_name VARCHAR(20) NOT NULL,
                          sup_address VARCHAR(20) NOT NULL,
                          sup_id VARCHAR(20) NOT NULL

);


CREATE TABLE item(
                     i_code VARCHAR(20) NOT NULL PRIMARY KEY,
                     i_description VARCHAR(50) NOT NULL ,
                     i_unit_price VARCHAR(20) NOT NULL,
                     i_qty_on_hand VARCHAR(20) NOT NULL
);


CREATE TABLE supplier_details(
                                 sup_contact VARCHAR(10),
                                 i_code VARCHAR(20),
                                 sup_description VARCHAR(25),
                                 sup_qty VARCHAR (10),
                                 date VARCHAR(10),
                                 total_amount VARCHAR (20),
                                 FOREIGN KEY( sup_contact) references supplier( sup_contact) on delete cascade on update cascade,
                                 FOREIGN KEY(i_code) references item(i_code) on delete cascade on update cascade
);

CREATE TABLE repair_details(
                               r_id VARCHAR(20) PRIMARY KEY,
                               e_id VARCHAR(20),
                               r_description VARCHAR(50),
                               r_price VARCHAR (10),
                               r_date VARCHAR(10),
                               c_contact VARCHAR(10),
                               FOREIGN KEY(e_id) references employee(e_id) on delete cascade on update cascade,
                               FOREIGN KEY(c_contact ) references customer(c_contact ) on delete cascade on update cascade
);

create table orders(
                       o_id varchar(35) primary key,
                       c_contact VARCHAR(10),
                       date date not null,
                       constraint foreign key ( c_contact) references customer( c_contact)
                           on delete cascade on update cascade
);

create table order_details(
                             o_id varchar(35) not null ,
                             i_code varchar(35) not null,
                             qty int not null,
                             unit_price double not null,
                             constraint foreign key (o_id) references orders(o_id)
                                 on delete  cascade on update cascade,
                             constraint foreign key (i_code) references item(i_code)
                                 on delete cascade on update cascade
);

