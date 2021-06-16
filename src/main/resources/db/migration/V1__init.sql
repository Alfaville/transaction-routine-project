create table ACCOUNT (
   id bigint generated by default as identity,
    document_number bigint not null,
    primary key (id)
);

create table OPERATION_TYPE (
    id bigint generated by default as identity,
    description varchar(255) not null,
    primary key (id)
);

create table TRANSACTION (
    id bigint generated by default as identity,
    amount decimal(19,2) not null,
    event_date timestamp not null,
    account_id bigint not null,
    operation_type_id bigint not null,
    primary key (id)
);

alter table transaction
   add constraint FK_ACCOUNT_ID_TRANSACTION
   foreign key (account_id)
   references account;

alter table transaction
   add constraint FK_OPERATION_TYPE_ID_TRANSACTION
   foreign key (operation_type_id)
   references operation_type;
