create table usertbl(
userid varchar(128) primary key,
pw varchar(256),
regdate date,
upddate date
);

create table grouptbl(
userid VARCHAR(128) not null,
groupid varchar(128) not null,
regdate date,
upddate date,
constraint group_pk primary key(userid,groupid),
constraint user_fk foreign key(userid) references usertbl(userid) on delete cascade on update restrict
);

create table categorytbl(
categoryid integer primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
userid varchar(128) not null,
categoryname varchar(12) not null,
regdate date,
upddata date,
constraint category_user_fk foreign key(userid) references usertbl(userid) on delete cascade on update restrict
);

create table feedtbl(
rssid INTEGER NOT NULL primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
userid VARCHAR(128) not null,
TITLE VARCHAR(1024) NOT NULL,
URL VARCHAR(1024) NOT NULL,
description varchar(1024) ,
categoryid integer,
unread_count integer default 0,
regdate date,
upddate date,
unique(userid,url),
foreign key(userid) references usertbl(userid) on delete cascade on update restrict,
foreign key(categoryid) references categorytbl(categoryid) on delete cascade on update restrict
);

create table contentstbl(
contentsid INTEGER NOT NULL primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
rssid integer not null,
--未読既読フラグ。false=未読の意味
readflg boolean not null default false,
title varchar(1024),
url varchar(1024),
contents varchar(32672),
publishdate date,
regdate date,
upddate date,
unique(rssid,url),
foreign key (rssid) references feedtbl(rssid) on delete cascade on update restrict
);

--contentstblが更新されたらfeedtblのunread_countをtriggerで更新する
create trigger trg_update_unreadcount
after update on contentstbl
referencing new as updaterow
for each row
update feedtbl set unread_count=(select count(*) from contentstbl where rssid=updaterow.rssid and readflg=false) where rssid=updaterow.rssid;

--contentstblに挿入されたらfeedtblのunread_countをtriggerで更新する
create trigger trg_insert_contetstbl
after insert on contentstbl
referencing new as new
for each row
update feedtbl set unread_count=(select count(*) from contentstbl where rssid=new.rssid and readflg=false) where rssid=new.rssid;
