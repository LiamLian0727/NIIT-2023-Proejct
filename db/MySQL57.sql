CREATE DATABASE IF NOT EXISTS NIIT
    CHARACTER SET utf8
    COLLATE utf8_bin;

use NIIT;

CREATE TABLE IF NOT EXISTS `user`(
                                     `UserName` VARCHAR(20),
                                     `Password` VARCHAR(20) NOT NULL,
                                     `EmailID` VARCHAR(20) NOT NULL,
                                     PRIMARY KEY ( `UserName` ));

insert into `user`(username, password, emailid)
    values
    ('Liam','niit1234','1225419368@qq.com');

SELECT * from user;

drop table imdb_movies;

create table imdb_movies (
                             imdb_title_id varchar(256) primary key,
                             title text,
                             original_title text,
                             year text,
                             date_published text,
                             genre text,
                             duration text,
                             country text,
                             language text,
                             director text,
                             writer text,
                             production_company text,
                             actors text,
                             description text,
                             avg_vote text,
                             votes text,
                             budget text,
                             usa_gross_income text,
                             worlwide_gross_income text,
                             metascore text,
                             reviews_from_users text,
                             reviews_from_critics text
);

select * from imdb_movies;