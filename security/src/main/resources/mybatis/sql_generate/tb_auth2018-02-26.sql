USE security;

CREATE TABLE `tb_auth` (
  `AUTH_NO` int(11) NOT NULL AUTO_INCREMENT COMMENT '권한 일련 번호',
  `AUTH_NM` varchar(50) NOT NULL COMMENT '권한 명',
  `AUTH_EXPLANATION` varchar(50) NOT NULL COMMENT '권한 설명',
  PRIMARY KEY (`AUTH_NO`),
  UNIQUE KEY `AUTH_NM` (`AUTH_NM`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='권한 테이블';


INSERT INTO `tb_auth`(`AUTH_NO`,`AUTH_NM`,`AUTH_EXPLANATION`) VALUES (1,'ROLE_MANAGER','매니저에게 부여되는 권한 입니다. 대다수 회원정보를 관리할 수 있습니다.');
INSERT INTO `tb_auth`(`AUTH_NO`,`AUTH_NM`,`AUTH_EXPLANATION`) VALUES (2,'ROLE_USER','회원에게 부여되는 권한 입니다. 일반적인 조회,삭제,수정 권한을 지니고 있습니다.');
INSERT INTO `tb_auth`(`AUTH_NO`,`AUTH_NM`,`AUTH_EXPLANATION`) VALUES (3,'ROLE_ADMIN','관리자에게 주어지는 최고권한 입니다. 관리자 이외 누구에게도 부여하지 마십시오.');
INSERT INTO `tb_auth`(`AUTH_NO`,`AUTH_NM`,`AUTH_EXPLANATION`) VALUES (4,'ROLE_ANONYMOUS','미로그인한 사람을 지칭하는 용어 입니다.');
