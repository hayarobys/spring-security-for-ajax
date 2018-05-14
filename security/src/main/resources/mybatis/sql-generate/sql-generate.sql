-- MySQL dump 10.13  Distrib 5.7.19, for Win64 (x86_64)

DROP TABLE IF EXISTS `tb_auth`;
CREATE TABLE `tb_auth` (
  `AUTH_NO` int(11) NOT NULL AUTO_INCREMENT COMMENT '권한 일련 번호',
  `AUTH_NM` varchar(50) NOT NULL COMMENT '권한 명',
  `AUTH_EXPLANATION` varchar(50) NOT NULL COMMENT '권한 설명',
  PRIMARY KEY (`AUTH_NO`),
  UNIQUE KEY `AUTH_NM` (`AUTH_NM`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='권한 테이블';


LOCK TABLES `tb_auth` WRITE;
INSERT INTO `tb_auth` VALUES (1,'ROLE_MANAGER','매니저에게 부여되는 권한 입니다. 대다수 회원정보를 관리할 수 있습니다.'),(2,'ROLE_USER','회원에게 부여되는 권한 입니다. 일반적인 조회,삭제,수정 권한을 지니고 있습니다.'),(3,'ROLE_ADMIN','관리자에게 주어지는 최고권한 입니다. 관리자 이외 누구에게도 부여하지 마십시오.'),(4,'ROLE_ANONYMOUS','미로그인한 사람을 지칭하는 용어 입니다.'),(7,'ROLE_GHOST','유령회원에게 부여되는 권한 입니다. 본인 글의 조회와 정회원 전환 신청이 가능합니다.');
UNLOCK TABLES;


DROP TABLE IF EXISTS `tb_block_member`;
CREATE TABLE `tb_block_member` (
  `BLOCK_NO` int(11) NOT NULL AUTO_INCREMENT COMMENT '차단 일련 번호',
  `MEM_NO` int(11) NOT NULL COMMENT '계정 일련 번호',
  `BLOCK_START_DATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '차단 시작 일자',
  `BLOCK_EXPIRE_DATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '차단 만료 일자',
  `BLOCK_CAUSE` varchar(300) NOT NULL COMMENT '차단 사유',
  PRIMARY KEY (`BLOCK_NO`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='차단 계정 테이블';


LOCK TABLES `tb_block_member` WRITE;
INSERT INTO `tb_block_member` VALUES (1,11,'2018-04-26 05:33:32','2018-03-30 06:54:54','과거'),(2,11,'2018-07-25 06:54:54','2019-03-30 06:54:54','미래1'),(3,11,'2019-04-23 06:54:54','2019-04-30 06:54:54','미래2'),(4,14,'2018-05-03 07:06:43','2018-05-31 06:56:49','현재'),(5,14,'2018-05-24 07:05:43','2018-05-27 06:57:06','미래2'),(6,14,'2018-05-24 07:11:43','2018-05-30 06:57:06','미래3'),(7,11,'2018-05-14 06:51:33','2018-06-01 06:51:33','현재');
UNLOCK TABLES;


DROP TABLE IF EXISTS `tb_member`;
CREATE TABLE `tb_member` (
  `MEM_NO` int(11) NOT NULL AUTO_INCREMENT COMMENT '계정 일련 번호',
  `MEM_ID` varchar(50) NOT NULL COMMENT '계정 ID',
  `MEM_PASSWORD` varchar(300) NOT NULL COMMENT '계정 PW',
  `MEM_NICKNM` varchar(30) NOT NULL COMMENT '계정 이름',
  `MEM_STATE` varchar(20) NOT NULL COMMENT '계정 상태',
  `LAST_LOGIN_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '마지막 로그인 일자',
  PRIMARY KEY (`MEM_NO`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='계정 테이블';


LOCK TABLES `tb_member` WRITE;
INSERT INTO `tb_member` VALUES (1,'admin','$2a$10$NZvPdl81VgihX9ZKchaYZunWsd1wmm2Ap4IWtjXR.8sHvFReE9awy','관리자','ACTIVE','2017-03-12 08:40:00'),(2,'user','$2a$10$nDi7TrWFH0mRXzk6Ncfd3uZke0JUmgPLxOMxudsSClhu0iKJpIddS','일반인','ACTIVE','2018-03-12 09:16:49'),(3,'manager','$2a$10$LP3.bRbdubi788K18uwi..cd.pCGYNeIUuBavTDuuiUXOSuxWqMIe','매니저','ACTIVE','2017-03-12 09:17:00'),(11,'tester','$2a$10$qRw0wtqj372RRGDggxMtW.WS/XGEnpbqvaHrZYoNTG8NKIzFbNdmO','테스터','ACTIVE','2018-03-14 06:23:51'),(14,'tester2','$2a$10$UwQYF9VDwvysO4tMQ45CCeeyNFRdWPGpwmhwvjf6Hz1CQ7yDoblYi','테스터2','ACTIVE','2018-05-04 04:18:15');
UNLOCK TABLES;


DROP TABLE IF EXISTS `tb_member_auth`;
CREATE TABLE `tb_member_auth` (
  `MEM_NO` int(11) NOT NULL COMMENT '계정 일련 번호',
  `AUTH_NO` int(11) NOT NULL COMMENT '권한 일련 번호',
  PRIMARY KEY (`MEM_NO`,`AUTH_NO`),
  KEY `FK_TB_MEMBER_AUTH_AUTH_NO_TB_AUTH_AUTH_NO` (`AUTH_NO`),
  CONSTRAINT `FK_TB_MEMBER_AUTH_AUTH_NO_TB_AUTH_AUTH_NO` FOREIGN KEY (`AUTH_NO`) REFERENCES `tb_auth` (`AUTH_NO`),
  CONSTRAINT `FK_TB_MEMBER_AUTH_MEM_NO_TB_MEMBER_MEM_NO` FOREIGN KEY (`MEM_NO`) REFERENCES `tb_member` (`MEM_NO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='계정-권한 테이블';


LOCK TABLES `tb_member_auth` WRITE;
INSERT INTO `tb_member_auth` VALUES (3,1),(2,2),(11,2),(14,2),(1,3);
UNLOCK TABLES;


DROP TABLE IF EXISTS `tb_resource`;
CREATE TABLE `tb_resource` (
  `RESOURCE_NO` int(11) NOT NULL AUTO_INCREMENT COMMENT '리소스 일련 번호',
  `HTTP_METHOD` varchar(10) NOT NULL COMMENT 'HTTP 메소드',
  `RESOURCE_NM` varchar(50) NOT NULL COMMENT '리소스 이름',
  `RESOURCE_PATTERN` varchar(100) NOT NULL COMMENT '리소스 패턴',
  `RESOURCE_TYPE` varchar(10) NOT NULL COMMENT '리소스 타입(url : URL, method : Method)',
  `SORT_ORDER` int(11) NOT NULL COMMENT '순서',
  PRIMARY KEY (`RESOURCE_NO`),
  UNIQUE KEY `HTTP_METHOD_NO` (`HTTP_METHOD`,`RESOURCE_PATTERN`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8 COMMENT='리소스 테이블';


LOCK TABLES `tb_resource` WRITE;
INSERT INTO `tb_resource` VALUES (2,'GET','관리자 페이지','/admin/**','url',40000),(4,'GET','메인 페이지','/main','url',30000),(5,'GET','모든 페이지','/**','url',10000),(11,'GET','리소스 관리 페이지 이동','/resource/edit','url',50000),(12,'GET','모든 리소스 목록 중 해당 페이지 만큼 반환','/resource/page/*','url',50110),(13,'GET','모든 리소스 목록 반환','/resource','url',50100),(17,'POST','리소스 추가','/resource','url',50200),(19,'PATCH','특정 리소스 수정','/resource/*','url',50300),(20,'DELETE','하나 또는 여러개의 리소스 삭제','/resource/*','url',50400),(21,'GET','HTTP 메소드 관리 페이지 이동','/http-method/edit','url',60000),(22,'GET','모든 HTTP 메소드 목록 반환','/http-method','url',60100),(23,'GET','리소스-권한 관리 페이지 이동','/resource-auth/edit','url',70000),(24,'GET','특정 리소스 접근에 필요한 권한 목록 반환','/resource/*/auth','url',70100),(25,'GET','특정 일련 번호의 HTTP 메소드 반환','/http-method/*','url',60200),(26,'PATCH','특정 리소스 접근에 필요한 권한 목록 업데이트','/resource/*/auth','url',70200),(27,'GET','권한 관리 페이지 이동','/auth/edit','url',80000),(28,'GET','모든 권한 목록 반환','/auth','url',80100),(29,'DELETE','특정 권한 삭제','/auth/*','url',80200),(30,'GET','계정별 권한 관리 페이지 이동','/member-auth/edit','url',90000),(31,'GET','특정 계정이 보유한 권한 목록 반환','/member/*/auth','url',90100),(32,'GET','특정 계정이 보유한 권한 목록 증 해당 페이지 만큼 반환','/member/*/auth/page/*','url',90110),(33,'PATCH','특정 계정이 보유한 권한 목록 업데이트','/member/*/auth','url',90200),(35,'GET','로그인 페이지','/login/edit','url',20000),(36,'POST','로그인 페이지','/login/edit','url',20001),(38,'PATCH','로그인 페이지','/login/edit','url',20003),(39,'DELETE','로그인 페이지','/login/edit','url',20004),(40,'PATCH','특정 권한 수정','/auth/*','url',80300),(41,'POST','권한 추가','/auth','url',80400),(43,'GET','계정 관리 페이지 이동','/member/edit','url',100000),(44,'GET','모든 계정 목록 반환(password제외)','/member','url',100100),(45,'GET','모든 계정 목록 중 해당 페이지 만큼 반환(password제외)','/member/page/*','url',100110),(46,'PATCH','특정 계정 수정','/member/*','url',100200),(47,'GET','회원가입 페이지 이동','/member/registraion','url',100300),(48,'POST','계정 중복 검사','/check/member/id','url',100400),(49,'POST','회원 등록','/member','url',100500),(50,'DELETE','특정 계정 삭제 / 회원 탈퇴','/member/*','url',100600),(51,'GET','차단 계정 관리 페이지로 이동','/block-member/edit','url',110000),(52,'GET','모든 차단 계정 목록 조회','/block-member','url',110100),(53,'POST','차단 계정 추가','/block-member','url',110200),(54,'PATCH','차단 계정 정보 수정','/block-member/*','url',110300),(55,'DELETE','차단 계정 정보 삭제','/block-member/*','url',110400);
UNLOCK TABLES;


DROP TABLE IF EXISTS `tb_resource_auth`;
CREATE TABLE `tb_resource_auth` (
  `RESOURCE_NO` int(11) NOT NULL COMMENT '리소스 일련 번호',
  `AUTH_NO` int(11) NOT NULL COMMENT '권한 일련 번호',
  PRIMARY KEY (`RESOURCE_NO`,`AUTH_NO`),
  KEY `FK_TB_RESOURCE_AUTH_AUTH_NO_TB_AUTH_AUTH_NO` (`AUTH_NO`),
  CONSTRAINT `FK_TB_RESOURCE_AUTH_AUTH_NO_TB_AUTH_AUTH_NO` FOREIGN KEY (`AUTH_NO`) REFERENCES `tb_auth` (`AUTH_NO`),
  CONSTRAINT `FK_TB_RESOURCE_AUTH_RESOURCE_NO_TB_RESOURCE_RESOURCE_NO` FOREIGN KEY (`RESOURCE_NO`) REFERENCES `tb_resource` (`RESOURCE_NO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='리소스-권한 테이블';


LOCK TABLES `tb_resource_auth` WRITE;
INSERT INTO `tb_resource_auth` VALUES (2,1),(4,1),(5,1),(51,1),(52,1),(53,1),(54,1),(55,1),(4,2),(5,2),(2,3),(4,3),(5,3),(11,3),(12,3),(13,3),(17,3),(19,3),(20,3),(21,3),(22,3),(23,3),(24,3),(25,3),(26,3),(27,3),(28,3),(29,3),(30,3),(31,3),(32,3),(33,3),(40,3),(41,3),(43,3),(44,3),(45,3),(46,3),(48,3),(49,3),(50,3),(51,3),(52,3),(53,3),(54,3),(55,3),(4,4),(5,4),(35,4),(36,4),(38,4),(39,4),(47,4),(5,7);
UNLOCK TABLES;

