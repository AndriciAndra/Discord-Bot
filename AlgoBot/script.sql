create table COMMANDS (id INTEGER PRIMARY KEY,
                       name VARCHAR2(256) UNIQUE,
                       information VARCHAR2(1024),
                       link VARCHAR2(1024));

select * from COMMANDS;
select name from COMMANDS;

--drop table commands;