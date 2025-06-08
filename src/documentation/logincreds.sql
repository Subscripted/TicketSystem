create table logincreds
(
    email         varchar(255) null,
    password      varchar(200) null,
    client_id     varchar(255) null,
    client_secret varchar(255) null,
    blocked       tinyint      null,
    insert_date   datetime     not null,
    last_updated  datetime     not null
);