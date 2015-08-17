/*==============================================================*/
/* Table: CMSESION                                               */
/*==============================================================*/
create table CMSESION (
   ID_SESION           	INT4                 not null,
   ID_CLI               INT4                 null,
   CLAVE_SESION         VARCHAR(4)           null,
   FECHA_EXPIRACION     DATE                 null,
   constraint PK_CMSESION primary key (ID_SESION)
);

CREATE SEQUENCE public.sec_cmsesion
   INCREMENT 1
   START 1;

ALTER TABLE cmsesion
   ALTER COLUMN id_sesion SET DEFAULT nextval('sec_cmsesion');

