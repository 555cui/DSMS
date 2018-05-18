create database DSMS;
use DSMS;
create table table_group(
    id nvarchar(36) primary key,
    name nvarchar(128) not null unique,
    description nvarchar(512),

    createTime timestamp not null DEFAULT CURRENT_TIMESTAMP,
    updateTime timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

insert into table_group(id, name, description)
values('00000000-0000-0000-0000-00000000', 'root', '超级用户组'),
('00000000-0000-0000-0000-00000001', 'user', '普通用户组');

create table table_user(
    id nvarchar(36) primary key,
    name nvarchar(128) not null unique,
    password nvarchar(128) not null,
    email nvarchar(128) not null unique,
    groupId nvarchar(36) not null DEFAULT '00000000-0000-0000-0000-00000001',
    foreign key(groupId) references table_group(id) ON DELETE CASCADE ON UPDATE CASCADE,

    createTime timestamp not null DEFAULT CURRENT_TIMESTAMP,
    updateTime timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

insert into table_user(id, name, password, email, groupId)
values('00000000-0000-0000-0000-00000000', 'root', 'toor', 'root@ubuntu.com', '00000000-0000-0000-0000-00000000');

create table table_device_group(
    id nvarchar(36) primary key,
    name nvarchar(128) not null,
    userId nvarchar(36) not null,
    unique(name, userId),
    foreign key(userId) references table_user(id) ON DELETE CASCADE,

    createTime timestamp not null DEFAULT CURRENT_TIMESTAMP ,
    updateTime timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table table_device(
    id nvarchar(36) primary key,
    name nvarchar(128) not null,
    width int not null,
    height int not null,
    mac nvarchar(36) unique,
    address nvarchar(36),
    userId nvarchar(36) not null,
    groupId nvarchar(36) not null,
    unique(name,userId),
    foreign key(userId) references table_user(id) ON DELETE CASCADE,
    foreign key(groupId) references table_device_group(id) ON DELETE CASCADE,

    createTime timestamp not null DEFAULT CURRENT_TIMESTAMP,
    updateTime timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table table_media(
    id nvarchar(36) primary key,
    name nvarchar(128) not null,
    userId nvarchar(36) not null,
    type nvarchar(10) not null,
    _type nvarchar(10) not null,
    width int not null,
    height int not null,
    etcData int not null DEFAULT 0,
    url nvarchar(400),
    imageUrl nvarchar(128),
    unique(userId, name),
    foreign key(userId) references table_user(id) ON DELETE CASCADE,

    createTime timestamp not null DEFAULT CURRENT_TIMESTAMP ,
    updateTime timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table table_media_group(
    id nvarchar(36) not null,
    mediaId nvarchar(36) not null,
    mediaIndex int not null,
    primary key(mediaId, id),
    unique(id, mediaId),
    foreign key(id) references table_media(id) ON DELETE  CASCADE ,
    foreign key(mediaId) references table_media(id) ON DELETE CASCADE ,

    createTime timestamp not null DEFAULT CURRENT_TIMESTAMP ,
    updateTime timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table table_template_group(
    id nvarchar(36) primary key,
    userId nvarchar(36) not null,
    name nvarchar(128) not null,
    unique(userId, name),
    foreign key(userId) references table_user(id) ON DELETE CASCADE,

    createTime timestamp not null DEFAULT CURRENT_TIMESTAMP ,
    updateTime timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table table_template(
    id nvarchar(36) primary key,
    userId nvarchar(36) not null,
    groupId nvarchar(36) not null,
    name nvarchar(36) not null,
    width int not null,
    height int not null,
    background_color nvarchar(16) not null,
    unique(userId, groupId, name),
    foreign key(userId) references table_user(id) ON DELETE CASCADE,
    foreign key(groupId) references table_template_group(id) ON DELETE CASCADE,

    createTime timestamp not null DEFAULT CURRENT_TIMESTAMP ,
    updateTime timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table table_template_element(
    id nvarchar(36) primary key,
    templateId nvarchar(36) not null,
    mediaId nvarchar(36) not null,
    width int not null,
    height int not null,
    _left int not null,
    top int not null,
    z_index int not null,
    foreign key (templateId) references table_template(id) ON DELETE CASCADE,
    foreign key (mediaId) references table_media(id),

    createTime timestamp not null DEFAULT CURRENT_TIMESTAMP ,
    updateTime timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table table_schedule(
    id nvarchar(36) primary key,
    userId nvarchar(36) not null,
    deviceId nvarchar(36),
    deviceGroupId nvarchar(36),
    week int not null,
    startDate timestamp not null DEFAULT CURRENT_TIMESTAMP,
    endDate timestamp not null DEFAULT CURRENT_TIMESTAMP,

    foreign key (userId) references table_user(id) ON DELETE CASCADE ,
    foreign key (deviceId) references table_device(id) ON DELETE CASCADE ,
    foreign key (deviceGroupId) references table_device_group(id) ON DELETE CASCADE ,

    createTime timestamp not null DEFAULT CURRENT_TIMESTAMP ,
    updateTime timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table table_schedule_program(
    scheduleId nvarchar(36),
    startTime nvarchar(10) not null,
    endTime nvarchar(10) not null,
    templateId nvarchar(36) not null,

    foreign key (scheduleId) references table_schedule(id) ON DELETE CASCADE ,
    foreign key (templateId) references table_template(id) ON DELETE CASCADE ,

    createTime timestamp not null DEFAULT CURRENT_TIMESTAMP ,
    updateTime timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table table_schedule_temp(
    id nvarchar(36) primary key,
    userId nvarchar(36) not null,
    deviceId nvarchar(36),
    deviceGroupId nvarchar(36),
    templateId nvarchar(36) not null,
    date timestamp not null,
    startTime int not null,
    endTime int not null,

    foreign key (userId) references table_user(id) ON DELETE CASCADE ,
    foreign key (templateId) references table_template(id) ON DELETE CASCADE ,

    createTime timestamp not null DEFAULT CURRENT_TIMESTAMP ,
    updateTime timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);