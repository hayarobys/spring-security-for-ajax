
USE security;

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


INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (2,'GET','관리자 페이지','/admin/**','url',40000);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (4,'GET','메인 페이지','/main','url',30000);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (5,'GET','모든 페이지','/**','url',10000);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (11,'GET','리소스 관리 페이지 이동','/resource/edit','url',50000);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (12,'GET','모든 리소스 목록 중 해당 페이지 만큼 반환','/resource/page/*','url',50110);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (13,'GET','모든 리소스 목록 반환','/resource','url',50100);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (17,'POST','리소스 추가','/resource','url',50200);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (19,'PATCH','특정 리소스 수정','/resource/*','url',50300);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (20,'DELETE','하나 또는 여러개의 리소스 삭제','/resource/*','url',50400);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (21,'GET','HTTP 메소드 관리 페이지 이동','/http-method/edit','url',60000);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (22,'GET','모든 HTTP 메소드 목록 반환','/http-method','url',60100);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (23,'GET','리소스-권한 관리 페이지 이동','/resource-auth/edit','url',70000);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (24,'GET','특정 리소스 접근에 필요한 권한 목록 반환','/resource/*/auth','url',70100);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (25,'GET','특정 일련 번호의 HTTP 메소드 반환','/http-method/*','url',60200);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (26,'PATCH','특정 리소스 접근에 필요한 권한 목록 업데이트','/resource/*/auth','url',70200);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (27,'GET','권한 관리 페이지 이동','/auth/edit','url',80000);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (28,'GET','모든 권한 목록 반환','/auth','url',80100);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (29,'DELETE','특정 권한 삭제','/auth/*','url',80200);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (30,'GET','계정별 권한 관리 페이지 이동','/member-auth/edit','url',90000);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (31,'GET','특정 계정이 보유한 권한 목록 반환','/member/*/auth','url',90100);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (32,'GET','특정 계정이 보유한 권한 목록 증 해당 페이지 만큼 반환','/member/*/auth/page/*','url',90110);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (33,'PATCH','특정 계정이 보유한 권한 목록 업데이트','/member/*/auth','url',90200);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (35,'GET','로그인 페이지','/login/edit','url',20000);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (36,'POST','로그인 페이지','/login/edit','url',20001);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (38,'PATCH','로그인 페이지','/login/edit','url',20003);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (39,'DELETE','로그인 페이지','/login/edit','url',20004);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (40,'PATCH','특정 권한 수정','/auth/*','url',80300);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (41,'POST','권한 추가','/auth','url',80400);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (43,'GET','계정 관리 페이지 이동','/member/edit','url',100000);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (44,'GET','모든 계정 목록 반환(password제외)','/member','url',100100);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (45,'GET','모든 계정 목록 중 해당 페이지 만큼 반환(password제외)','/member/page/*','url',100110);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (46,'PATCH','특정 계정 수정','/member/*','url',100200);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (47,'GET','회원가입 페이지 이동','/member/registraion','url',100300);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (48,'POST','계정 중복 검사','/check/member/id','url',100400);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (49,'POST','회원 등록','/member','url',100500);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (50,'DELETE','특정 계정 삭제 / 회원 탈퇴','/member/*','url',100600);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (51,'GET','차단 계정 관리 페이지로 이동','/block-member/edit','url',110000);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (52,'GET','모든 차단 계정 목록 조회','/block-member','url',110100);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (53,'POST','차단 계정 추가','/block-member','url',110200);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (54,'PATCH','차단 계정 정보 수정','/block-member/*','url',110300);
INSERT INTO tb_resource(RESOURCE_NO,HTTP_METHOD,RESOURCE_NM,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) VALUES (55,'DELETE','차단 계정 정보 삭제','/block-member/*','url',110400);
