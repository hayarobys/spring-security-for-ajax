 INSERT INTO
  TB_MEMBER(
    MEM_ID,
    MEM_PASSWORD,
    MEM_NICKNM,
    MEM_ENABLE
  )
 VALUES(
  'admin',
  'admin',
  '관리자',
  'Y'
 );
 
 INSERT INTO
  TB_MEMBER(
    MEM_ID,
    MEM_PASSWORD,
    MEM_NICKNM,
    MEM_ENABLE
  )
 VALUES(
  'user',
  'user',
  '일반인',
  'Y'
 );
 
 
 
 INSERT INTO
  TB_AUTH(
    AUTH_NM,
    AUTH_EXPLANATION
  )
 VALUES(
  'ROLE_ADMIN',
  '관리자에게 주어지는 최고권한 입니다. 관리자 이외 누구에게도 부여하지 마십시오.'
 );
 
 INSERT INTO
  TB_AUTH(
    AUTH_NM,
    AUTH_EXPLANATION
  )
 VALUES(
  'ROLE_USER',
  '회원에게 부여되는 권한 입니다. 일반적인 조회,삭제,수정 권한을 지니고 있습니다.'
 ); 
 
 INSERT INTO
  TB_AUTH(
    AUTH_NM,
    AUTH_EXPLANATION
  )
 VALUES(
  'ROLE_MANAGER',
  '매니저에게 부여되는 권한 입니다. 대다수 회원정보를 관리할 수 있습니다.'
 );
 
 INSERT INTO
  TB_AUTH(
    AUTH_NM,
    AUTH_EXPLANATION
  )
 VALUES(
  'ANONYMOUS',
  '미로그인한 사람을 지칭하는 용어 입니다.'
 );

 
 

-- 아이디와 권한 명으로 TB_MEMBER_AUTH 테이블에 MEM_NO - AUTH_NO 매핑 추가
INSERT INTO
  TB_MEMBER_AUTH( MEM_NO, AUTH_NO )
SELECT
  M.MEM_NO,
  A.AUTH_NO
FROM
  TB_MEMBER AS M
CROSS JOIN
  TB_AUTH AS A
WHERE
    M.MEM_ENABLE = 'Y'
  AND
    M.MEM_ID = 'admin'
  AND
    A.AUTH_NM = 'ROLE_ADMIN'
;

INSERT INTO
  TB_MEMBER_AUTH( MEM_NO, AUTH_NO )
SELECT
  M.MEM_NO,
  A.AUTH_NO
FROM
  TB_MEMBER AS M
CROSS JOIN
  TB_AUTH AS A
WHERE
    M.MEM_ENABLE = 'Y'
  AND
    M.MEM_ID = 'admin'
  AND
    A.AUTH_NM = 'ROLE_USER'
;

INSERT INTO
  TB_MEMBER_AUTH( MEM_NO, AUTH_NO )
SELECT
  M.MEM_NO,
  A.AUTH_NO
FROM
  TB_MEMBER AS M
CROSS JOIN
  TB_AUTH AS A
WHERE
    M.MEM_ENABLE = 'Y'
  AND
    M.MEM_ID = 'user'
  AND
    A.AUTH_NM = 'ROLE_USER'
;
  
  
  

INSERT INTO
  TB_RESOURCE(
    RESOURCE_TYPE,
    RESOURCE_PATTERN,
    RESOURCE_NM,
    SORT_ORDER
  )
VALUES(
  'url',
  '/admin/hello',
  'hello 페이지',
  '10000'
);

INSERT INTO
  TB_RESOURCE(
    RESOURCE_TYPE,
    RESOURCE_PATTERN,
    RESOURCE_NM,
    SORT_ORDER
  )
VALUES(
  'url',
  '/admin/**',
  '관리자 페이지',
  '10100'
);

INSERT INTO
  TB_RESOURCE(
    RESOURCE_TYPE,
    RESOURCE_PATTERN,
    RESOURCE_NM,
    SORT_ORDER
  )
VALUES(
  'url',
  '/login',
  '로그인 페이지',
  '20000'
);

INSERT INTO
  TB_RESOURCE(
    RESOURCE_TYPE,
    RESOURCE_PATTERN,
    RESOURCE_NM,
    SORT_ORDER
  )
VALUES(
  'url',
  '/main',
  '메인 페이지',
  '30000'
);

INSERT INTO
  TB_RESOURCE(
    RESOURCE_TYPE,
    RESOURCE_PATTERN,
    RESOURCE_NM,
    SORT_ORDER
  )
VALUES(
  'url',
  '/**',
  '모든 페이지',
  '40000'
);

INSERT INTO
  TB_RESOURCE_AUTH(
    RESOURCE_NO,
    AUTH_NO
  )
VALUES
  (3,4),
  (4,4),
  (5,4),
  (1,3),
  (2,3),
  (3,3),
  (4,3),
  (5,3),
  (3,2),
  (4,2),
  (5,2),
  (1,1),
  (2,1),
  (3,1),
  (4,1),
  (5,1);