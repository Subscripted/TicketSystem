create table api_tokens
(
    id           bigint auto_increment primary key,
    email        varchar(255) null,
    expires_at   datetime(6)  null,
    token        varchar(255) null,
    insert_date  datetime     not null,
    last_updated datetime     not null
)