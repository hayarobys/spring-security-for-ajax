
USE security;

CREATE TABLE `tb_resource_auth` (
  `RESOURCE_NO` int(11) NOT NULL COMMENT '리소스 일련 번호',
  `AUTH_NO` int(11) NOT NULL COMMENT '권한 일련 번호',
  PRIMARY KEY (`RESOURCE_NO`,`AUTH_NO`),
  KEY `FK_TB_RESOURCE_AUTH_AUTH_NO_TB_AUTH_AUTH_NO` (`AUTH_NO`),
  CONSTRAINT `FK_TB_RESOURCE_AUTH_AUTH_NO_TB_AUTH_AUTH_NO` FOREIGN KEY (`AUTH_NO`) REFERENCES `tb_auth` (`AUTH_NO`),
  CONSTRAINT `FK_TB_RESOURCE_AUTH_RESOURCE_NO_TB_RESOURCE_RESOURCE_NO` FOREIGN KEY (`RESOURCE_NO`) REFERENCES `tb_resource` (`RESOURCE_NO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='리소스-권한 테이블';


INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (2,1);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (4,1);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (5,1);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (51,1);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (52,1);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (53,1);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (54,1);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (55,1);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (4,2);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (5,2);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (2,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (4,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (5,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (11,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (12,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (13,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (17,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (19,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (20,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (21,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (22,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (23,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (24,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (25,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (26,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (27,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (28,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (29,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (30,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (31,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (32,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (33,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (40,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (41,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (43,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (44,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (45,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (46,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (48,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (49,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (50,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (51,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (52,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (53,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (54,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (55,3);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (4,4);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (5,4);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (35,4);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (36,4);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (38,4);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (39,4);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (47,4);
INSERT INTO tb_resource_auth(RESOURCE_NO,AUTH_NO) VALUES (5,7);
