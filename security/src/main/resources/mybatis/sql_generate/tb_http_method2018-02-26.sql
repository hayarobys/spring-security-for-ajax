USE security;

CREATE TABLE `tb_http_method` (
  `HTTP_METHOD_NO` int(11) NOT NULL AUTO_INCREMENT COMMENT 'HTTP 메소드 일련 번호',
  `HTTP_METHOD_PATTERN` varchar(10) NOT NULL COMMENT 'HTTP 메소드 패턴',
  `HTTP_METHOD_EXPLANATION` varchar(50) NOT NULL COMMENT 'HTTP 메소드 설명',
  PRIMARY KEY (`HTTP_METHOD_NO`),
  UNIQUE KEY `HTTP_METHOD_PATTERN` (`HTTP_METHOD_PATTERN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='HTTP METHOD 테이블';

INSERT INTO `tb_http_method`(`HTTP_METHOD_NO`,`HTTP_METHOD_PATTERN`,`HTTP_METHOD_EXPLANATION`) VALUES (1,'GET','조회');
INSERT INTO `tb_http_method`(`HTTP_METHOD_NO`,`HTTP_METHOD_PATTERN`,`HTTP_METHOD_EXPLANATION`) VALUES (2,'POST','등록');
INSERT INTO `tb_http_method`(`HTTP_METHOD_NO`,`HTTP_METHOD_PATTERN`,`HTTP_METHOD_EXPLANATION`) VALUES (3,'PUT','수정');
INSERT INTO `tb_http_method`(`HTTP_METHOD_NO`,`HTTP_METHOD_PATTERN`,`HTTP_METHOD_EXPLANATION`) VALUES (4,'PATCH','수정(권장)');
INSERT INTO `tb_http_method`(`HTTP_METHOD_NO`,`HTTP_METHOD_PATTERN`,`HTTP_METHOD_EXPLANATION`) VALUES (5,'DELETE','삭제');

