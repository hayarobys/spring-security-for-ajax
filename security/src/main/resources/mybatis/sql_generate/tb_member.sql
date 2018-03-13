Select MEM_NO, MEM_ID, MEM_PASSWORD, MEM_NICKNM, MEM_STATE, LAST_LOGIN_DATE from security.tb_member
USE security;

CREATE TABLE `tb_member` (
  `MEM_NO` int(11) NOT NULL AUTO_INCREMENT COMMENT '계정 일련 번호',
  `MEM_ID` varchar(50) NOT NULL COMMENT '계정 ID',
  `MEM_PASSWORD` varchar(300) NOT NULL COMMENT '계정 PW',
  `MEM_NICKNM` varchar(30) NOT NULL COMMENT '계정 이름',
  `MEM_STATE` varchar(20) NOT NULL COMMENT '계정 상',
  `LAST_LOGIN_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '마지막 로그인 일',
  PRIMARY KEY (`MEM_NO`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='계정 테이블';


INSERT INTO tb_member(MEM_NO,MEM_ID,MEM_PASSWORD,MEM_NICKNM,MEM_STATE,LAST_LOGIN_DATE) VALUES (1,'admin','$2a$10$NZvPdl81VgihX9ZKchaYZunWsd1wmm2Ap4IWtjXR.8sHvFReE9awy','관리자','ACTIVE','2018-03-12 17:40:00');
INSERT INTO tb_member(MEM_NO,MEM_ID,MEM_PASSWORD,MEM_NICKNM,MEM_STATE,LAST_LOGIN_DATE) VALUES (2,'user','$2a$10$nDi7TrWFH0mRXzk6Ncfd3uZke0JUmgPLxOMxudsSClhu0iKJpIddS','일반인','ACTIVE','2018-03-12 18:16:49');
INSERT INTO tb_member(MEM_NO,MEM_ID,MEM_PASSWORD,MEM_NICKNM,MEM_STATE,LAST_LOGIN_DATE) VALUES (3,'manager','$2a$10$LP3.bRbdubi788K18uwi..cd.pCGYNeIUuBavTDuuiUXOSuxWqMIe','매니저','ACTIVE','2018-03-12 18:17:00');
