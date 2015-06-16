/*==============================================================*/
/* DBMS name:      PostgreSQL 8                                 */
/* Created on:     14/06/2015 16:31:53                          */
/*==============================================================*/

/*==============================================================*/
/* Table: CLIENTE                                               */
/*==============================================================*/
create table CLIENTE (
   ID_CLI               INT4                 not null,
   NRO_CUENTA           TEXT                 null,
   NOMBRE               TEXT                 null,
   APELLIDO             TEXT                 null,
   CI_RUC               VARCHAR(10)          null,
   TELEFONO             VARCHAR(20)          null,
   CORREO               TEXT                 null,
   DIRECCION            TEXT                 null,
   ALIAS                TEXT                 null,
   PASS                 TEXT                 null,
   ULTM_IP              TEXT                 null,
   FECHA_ULTM_CON       DATE                 null,
   ACEPTA_POLITICAS     CHAR(1)              null,
   CM_MOVIL             CHAR(1)              null,
   CM_PIN               VARCHAR(4)           null,
   CM_PASS              TEXT                 null,
   CM_ULTM_IP           TEXT                 null,
   CM_FECHA_ULTM_CON    DATE                 null,
   BLOQUEDA             CHAR(1)              null,
   MOTIVO               TEXT                 null,
   constraint PK_CLIENTE primary key (ID_CLI)
);

/*==============================================================*/
/* Table: CUENTA                                                */
/*==============================================================*/
create table CUENTA (
   NRO_CUENTA           TEXT                 not null,
   ID_TIPO              INT4                 null,
   SALDO                NUMERIC(15,2)        null,
   constraint PK_CUENTA primary key (NRO_CUENTA)
);

/*==============================================================*/
/* Table: ESTADOTRANS                                           */
/*==============================================================*/
create table ESTADOTRANS (
   ID_EST               INT4                 not null,
   ESTADO               TEXT                 null,
   constraint PK_ESTADOTRANS primary key (ID_EST)
);

/*==============================================================*/
/* Table: ESTADOUSR                                             */
/*==============================================================*/
create table ESTADOUSR (
   ID_ESTADO            INT4                 not null,
   ESTADO               TEXT                 null,
   constraint PK_ESTADOUSR primary key (ID_ESTADO)
);

/*==============================================================*/
/* Table: HISTORIAL                                             */
/*==============================================================*/
create table HISTORIAL (
   ID_HIST              NUMERIC              not null,
   ESTADO               TEXT                 null,
   TIPO                 TEXT                 null,
   NRO_CUENTA           TEXT                 null,
   SALDO_ACTUAL         NUMERIC(15,2)        null,
   MONTO                NUMERIC(15,2)        null,
   NROC_DESTINO         TEXT                 null,
   SALDO_FINAL          NUMERIC(15,2)        null,
   FECHA                TIMESTAMP            null,
   constraint PK_HISTORIAL primary key (ID_HIST)
);

/*==============================================================*/
/* Table: PREGUNTAS                                             */
/*==============================================================*/
create table PREGUNTAS (
   ID_PREG              INT4                 not null,
   PREGUNTA             TEXT                 null,
   constraint PK_PREGUNTAS primary key (ID_PREG)
);

/*==============================================================*/
/* Table: RESPUESTAS                                            */
/*==============================================================*/
create table RESPUESTAS (
   ID_CLI               INT4                 not null,
   ID_PREG              INT4                 not null,
   RESPUESTA            TEXT                 null,
   constraint PK_RESPUESTAS primary key (ID_CLI, ID_PREG)
);

/*==============================================================*/
/* Table: TIPOCUENTA                                            */
/*==============================================================*/
create table TIPOCUENTA (
   ID_TIPO              INT4                 not null,
   TIPO                 TEXT                 null,
   constraint PK_TIPOCUENTA primary key (ID_TIPO)
);

/*==============================================================*/
/* Table: TIPOTRANS                                             */
/*==============================================================*/
create table TIPOTRANS (
   ID_TIPOTRANS         INT4                 not null,
   TIPOTRANS            TEXT                 null,
   constraint PK_TIPOTRANS primary key (ID_TIPOTRANS)
);

/*==============================================================*/
/* Table: TIPOUSR                                               */
/*==============================================================*/
create table TIPOUSR (
   ID_TIPO              INT4                 not null,
   TIPO                 TEXT                 null,
   constraint PK_TIPOUSR primary key (ID_TIPO)
);

/*==============================================================*/
/* Table: TRANSACCIONES                                         */
/*==============================================================*/
create table TRANSACCIONES (
   ID_TRANS             NUMERIC              not null,
   ID_EST               INT4                 null,
   ID_CLI               INT4                 null,
   ID_TIPOTRANS         INT4                 null,
   NRO_CUENTA           TEXT                 null,
   SALDO_ACTUAL         NUMERIC(15,2)        null,
   MONTO                NUMERIC(15,2)        null,
   NROC_DESTINO         TEXT                 null,
   SALDO_FINAL          NUMERIC(15,2)        null,
   FECHA                TIMESTAMP            null,
   constraint PK_TRANSACCIONES primary key (ID_TRANS)
);

/*==============================================================*/
/* Table: TRANSFERENCIA                                         */
/*==============================================================*/
create table TRANSFERENCIA (
   ID_TRANSF            NUMERIC              not null,
   ID_EST               INT4                 null,
   ID_CLI               INT4                 null,
   NRO_CUENTA           TEXT                 null,
   SALDO_ACTUAL         NUMERIC(15,2)        null,
   MONTO                NUMERIC(15,2)        null,
   NROC_DESTINO         TEXT                 null,
   SALDO_FINAL          NUMERIC(15,2)        null,
   FECHA                TIMESTAMP            null,
   TOKEN                TEXT                 null,
   INTENTOS             INT2                 null,
   constraint PK_TRANSFERENCIA primary key (ID_TRANSF)
);

/*==============================================================*/
/* Table: USUARIO                                               */
/*==============================================================*/
create table USUARIO (
   ID_USR               INT4                 not null,
   ID_TIPO              INT4                 null,
   ID_ESTADO            INT4                 null,
   NOMBRE               TEXT                 null,
   APELLIDO             TEXT                 null,
   ALIAS                TEXT                 null,
   PASS                 TEXT                 null,
   constraint PK_USUARIO primary key (ID_USR)
);

alter table CLIENTE
   add constraint FK_CLIENTE_REFERENCE_CUENTA foreign key (NRO_CUENTA)
      references CUENTA (NRO_CUENTA)
      on delete restrict on update restrict;

alter table CUENTA
   add constraint FK_CUENTA_REFERENCE_TIPOCUEN foreign key (ID_TIPO)
      references TIPOCUENTA (ID_TIPO)
      on delete restrict on update restrict;

alter table RESPUESTAS
   add constraint FK_RESPUEST_REFERENCE_CLIENTE foreign key (ID_CLI)
      references CLIENTE (ID_CLI)
      on delete restrict on update restrict;

alter table RESPUESTAS
   add constraint FK_RESPUEST_REFERENCE_PREGUNTA foreign key (ID_PREG)
      references PREGUNTAS (ID_PREG)
      on delete restrict on update restrict;

alter table TRANSACCIONES
   add constraint FK_TRANSACC_REFERENCE_ESTADOTR foreign key (ID_EST)
      references ESTADOTRANS (ID_EST)
      on delete restrict on update restrict;

alter table TRANSACCIONES
   add constraint FK_TRANSACC_REFERENCE_CLIENTE foreign key (ID_CLI)
      references CLIENTE (ID_CLI)
      on delete restrict on update restrict;

alter table TRANSACCIONES
   add constraint FK_TRANSACC_REFERENCE_TIPOTRAN foreign key (ID_TIPOTRANS)
      references TIPOTRANS (ID_TIPOTRANS)
      on delete restrict on update restrict;

alter table TRANSFERENCIA
   add constraint FK_TRANSFER_REFERENCE_CLIENTE foreign key (ID_CLI)
      references CLIENTE (ID_CLI)
      on delete restrict on update restrict;

alter table TRANSFERENCIA
   add constraint FK_TRANSFER_REFERENCE_ESTADOTR foreign key (ID_EST)
      references ESTADOTRANS (ID_EST)
      on delete restrict on update restrict;

alter table USUARIO
   add constraint FK_USUARIO_REFERENCE_TIPOUSR foreign key (ID_TIPO)
      references TIPOUSR (ID_TIPO)
      on delete restrict on update restrict;

alter table USUARIO
   add constraint FK_USUARIO_REFERENCE_ESTADOUS foreign key (ID_ESTADO)
      references ESTADOUSR (ID_ESTADO)
      on delete restrict on update restrict;
/*==============================================================*/
/* SECUENCIAS                                                   */
/*==============================================================*/

CREATE SEQUENCE public.sec_cliente
   INCREMENT 1
   START 1;

ALTER TABLE cliente
   ALTER COLUMN id_cli SET DEFAULT nextval('sec_cliente');
ALTER TABLE cliente
  DROP CONSTRAINT fk_cliente_reference_cuenta;
ALTER TABLE cliente
  ADD CONSTRAINT fk_cliente_reference_cuenta FOREIGN KEY (nro_cuenta)
      REFERENCES cuenta (nro_cuenta) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT;
   
CREATE SEQUENCE public.sec_estadotrans
   INCREMENT 1
   START 1;

ALTER TABLE estadotrans
   ALTER COLUMN id_est SET DEFAULT nextval('sec_estadotrans');
   
CREATE SEQUENCE public.sec_estadousr
   INCREMENT 1
   START 1;

ALTER TABLE estadousr
   ALTER COLUMN id_estado SET DEFAULT nextval('sec_estadousr');
   
CREATE SEQUENCE public.sec_historial
   INCREMENT 1
   START 1;

ALTER TABLE historial
   ALTER COLUMN id_hist SET DEFAULT nextval('sec_historial');
   
CREATE SEQUENCE public.sec_preguntas
   INCREMENT 1
   START 1;

ALTER TABLE preguntas
   ALTER COLUMN id_preg SET DEFAULT nextval('sec_preguntas');
   
CREATE SEQUENCE public.sec_tipocuenta
   INCREMENT 1
   START 1;

ALTER TABLE tipocuenta
   ALTER COLUMN id_tipo SET DEFAULT nextval('sec_tipocuenta');   

CREATE SEQUENCE public.sec_tipotrans
   INCREMENT 1
   START 1;
   
ALTER TABLE tipotrans
   ALTER COLUMN id_tipotrans SET DEFAULT nextval('sec_tipotrans'); 
   
CREATE SEQUENCE public.sec_tipousr
   INCREMENT 1
   START 1;

ALTER TABLE tipousr
   ALTER COLUMN id_tipo SET DEFAULT nextval('sec_tipousr');
   
CREATE SEQUENCE public.sec_transacciones
   INCREMENT 1
   START 1;

ALTER TABLE transacciones
   ALTER COLUMN id_trans SET DEFAULT nextval('sec_transacciones');
ALTER TABLE transacciones
  DROP CONSTRAINT fk_transacc_reference_cliente;
ALTER TABLE transacciones
  DROP CONSTRAINT fk_transacc_reference_estadotr;
ALTER TABLE transacciones
  DROP CONSTRAINT fk_transacc_reference_tipotran;
ALTER TABLE transacciones
  ADD CONSTRAINT fk_transacc_reference_cliente FOREIGN KEY (id_cli)
      REFERENCES cliente (id_cli) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE transacciones
  ADD CONSTRAINT fk_transacc_reference_estadotr FOREIGN KEY (id_est)
      REFERENCES estadotrans (id_est) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE transacciones
  ADD CONSTRAINT fk_transacc_reference_tipotran FOREIGN KEY (id_tipotrans)
      REFERENCES tipotrans (id_tipotrans) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT;
	  
CREATE SEQUENCE public.sec_transferencia
   INCREMENT 1
   START 1;

ALTER TABLE transferencia
   ALTER COLUMN id_transf SET DEFAULT nextval('sec_transferencia');
ALTER TABLE transferencia
  DROP CONSTRAINT fk_transfer_reference_cliente;
ALTER TABLE transferencia
  DROP CONSTRAINT fk_transfer_reference_estadotr;
ALTER TABLE transferencia
  ADD CONSTRAINT fk_transfer_reference_cliente FOREIGN KEY (id_cli)
      REFERENCES cliente (id_cli) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE transferencia
  ADD CONSTRAINT fk_transfer_reference_estadotr FOREIGN KEY (id_est)
      REFERENCES estadotrans (id_est) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT;
	  
CREATE SEQUENCE public.sec_usuario
   INCREMENT 1
   START 1;
   
ALTER TABLE usuario
   ALTER COLUMN id_usr SET DEFAULT nextval('sec_usuario');
ALTER TABLE usuario
  DROP CONSTRAINT fk_usuario_reference_estadous;
ALTER TABLE usuario
  DROP CONSTRAINT fk_usuario_reference_tipousr;
ALTER TABLE usuario
  ADD CONSTRAINT fk_usuario_reference_estadous FOREIGN KEY (id_estado)
      REFERENCES estadousr (id_estado) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE usuario
  ADD CONSTRAINT fk_usuario_reference_tipousr FOREIGN KEY (id_tipo)
      REFERENCES tipousr (id_tipo) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT;
