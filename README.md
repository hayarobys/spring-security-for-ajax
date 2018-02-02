# security_practice
1. 소개
	>URL별 접근 권한을 DB를 통해 관리 하도록 커스텀 한 Spring Security 프로젝트 입니다.
	>JWT기반 인증, HTTPS, XSS & CSRF방어, 기본 스키마 정보등이 포함되어 있습니다.

2. 특징
	* DB를 통한 URL별 권한 관리
	* JWT 쿠키 기반 인증 관리
	* HTTPS
	* XSS & CSRF 대비
	* 스키마 파일 첨부
	* XML기반 Bean 관리

3. 의존성
	* Spring Framework 4.3.12.RELEASE
	* Spring Security 4.2.4.RELEASE
	* Spring ORM 4.3.12.RELEASE
	* MariaDB Client 2.2.1
	* commons-dbcp2
	* MyBatis
	* jackson
	* [JJWT](https://github.com/jwtk/jjwt)
	* etc...

4. 개발환경
	* Spring Tool Suite 3.8.2
	* Tomcat v9.0
	* Java 8
	* MariaDB
	* Windows 10

5. 사용법
	1. 톰캣을 사용할 경우 server.xml에 다음의 코드를 추가하십시오. (인터넷을 참고해 HTTPS 인증서를 미리 생성해 두어야 합니다)
		<pre>&lt;Connector connectionTimeout="20000" port="8080" protocol="HTTP/1.1" redirectPort="8443"/&gt;
		&lt;Connector
			port="8443"
			protocol="org.apache.coyote.http11.Http11NioProtocol"
			SSLEnabled="true"
			clientAuth="false"
			maxThreads="150"
			keystoreFile="C:\Your Keystore File Path"
			keystorePass="Your Password"
			scheme="https"
			secure="true"
			sslProtocol="TLS"
		/&gt;</pre>
		
	2. HTTPS 인증서를 생성하고, 위 ㄱ. 항목에 그 경로와 비밀번호를 추가하십시오.
	3. src/main/resources/properties/database.properties에 본인의 데이터베이스 접속 정보를 입력하십시오.
		<pre>jdbc.driverClassName=org.mariadb.jdbc.Driver
		jdbc.url=jdbc:mariadb://your_ip:your_port/your_repository_name
		jdbc.username=my_id
		jdbc.password=myp@ssword1#</pre>
	4. http 8080 포트로 접속할 경우 https 8443 포트로 리디렉트 됩니다.
