create table podcast(
    id serial primary key,
    uuid varchar(1000) unique,
    title varchar(100),
    published bigint,
    description text,
    audioUrl varchar(1000),
    starred boolean
);

create table category(
    id serial primary key,
    name varchar(50)
);
