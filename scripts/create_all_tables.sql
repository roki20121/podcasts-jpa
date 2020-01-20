create table podcast(
    id serial primary key,
    uuid varchar(1000) unique,
    title varchar(100),
    published timestamp,
    description text,
    audioUrl varchar(1000),
    starred boolean,
    channel_id int
);

create table category(
    id serial primary key,
    name varchar(50)
);

create table channel(
    id serial primary key,
    url varchar(1000),
    title varchar(100),
    description text,
    last_published timestamp,
    starred boolean
);

create table podcast_category(
    podcast_id int,
    category_id int,
    constraint pk_podcast_category primary key (podcast_id,category_id)
);

alter table podcast_category
add constraint fk_podcast_category_podcast_id foreign key (podcast_id) references podcast(id);

alter table podcast_category
add constraint fk_podcast_category_category_id foreign key (category_id) references category(id);

alter table podcast
add constraint fk_podcast_channel_id foreign key (channel_id) references channel(id);