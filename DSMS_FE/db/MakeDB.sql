use DSMS;
create table table_group(
    id nvarchar(36) primary key,
    name nvarchar(128) not null unique,
    description nvarchar(512),

    operator nvarchar(128) not null,
    createTime timestamp not null DEFAULT CURRENT_TIMESTAMP,
    updateTime timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

insert into table_group(id, name, operator)
values('supergroup', 'root', 'root'),
('00000000-00000000-0000-0000-00000000', 'user', 'root');

create table table_user(
    id nvarchar(36) primary key,
    name nvarchar(128) not null unique,
    password nvarchar(128) not null,
    email nvarchar(128) not null unique,
    groupId nvarchar(36) not null DEFAULT '00000000-00000000-0000-0000-00000000',
    createTime timestamp not null DEFAULT CURRENT_TIMESTAMP,
    updateTime timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    foreign key(groupId) references table_group(id) ON DELETE CASCADE ON UPDATE CASCADE
);

insert into table_user(id, name, password, email, groupId)
values('superuser', 'root', 'toor', 'root@ubuntu.com', 'supergroup');

create table table_role(
    id nvarchar(36) primary key,
    name nvarchar(128) not null unique,
    level int not null,
    description nvarchar(512),

    operator nvarchar(128) not null,
    createTime timestamp not null DEFAULT CURRENT_TIMESTAMP,
    updateTime timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    foreign key(operator) references table_user(name) ON DELETE CASCADE ON UPDATE CASCADE
);

insert into table_role(id, name, level, operator)values
('default_select', 'select', 0, 'root'),
('default_update', 'update', 0, 'root'),
('default_delete', 'delete', 0, 'root'),
('default_insert', 'insert', 0, 'root');