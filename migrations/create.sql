create table chats
(
    chat_id  integer primary key,
    username text
);

create table links
(
    link_id serial primary key,
    url     text
);

create table chat_link
(
    link_id integer,
    chat_id integer,
    foreign key (link_id) references links (link_id),
    foreign key (chat_id) references chats (chat_id)
);