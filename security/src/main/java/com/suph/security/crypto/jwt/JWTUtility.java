package com.suph.security.crypto.jwt;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.DefaultClaims;

public class JWTUtility{
	private static final Logger LOGGER = LoggerFactory.getLogger(JWTUtility.class);
	
	/**
	 * 입력된 정보로 JWT(Json Web Token)를 생성합니다.
	 * 
	 * @param issuedAt		생성일
	 * @param expiration	만료일*
	 * @param issuer		생성자
	 * @param claims		비공개 클레임(server-client간 미리 약속한 임의의 정보)
	 * @return	JWT 반환
	 */
	public static String createJWT(
		Date issuedAt, Date expiration, String issuer,
		Map<String, Object> claims,
		String secret
	){
		/*
		Properties prop = new Properties();
		try(
			// Java7 부터 제공하는 stream auto close 기능
			InputStream input = new FileInputStream("config.properties");
		){
			prop.load(input);
			String customKey = prop.getProperty("customKey");
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		*/
		
		String token = Jwts.builder()
				.setHeaderParam("type", "JWT")
				.setClaims(claims)			// 비공개 클레임 (Map<String, Object>)
				.setExpiration(expiration)	// 만료일
				.setIssuedAt(issuedAt)		// 생성일
				.setIssuer(issuer)			// 생성자
				//.setAudience(audience)	// 대상자(소유자)
				.signWith(SignatureAlgorithm.HS256, JWTUtility.generateKey(secret))	// 암호화 방식, 개인키
				.compact();					// 생성
		
		LOGGER.debug("generated token {}", token);
		
		return token;
	}
	
	/**
	 * 주어진 SALT값으로 byte형식의 키를 생성합니다.
	 * @param salt
	 * @return
	 */
	protected static byte[] generateKey(String salt){
		byte[] key = null;
		try {
			key = salt.getBytes("UTF-8");
		}catch(UnsupportedEncodingException e){
			if(LOGGER.isInfoEnabled()){
				e.printStackTrace();
			}else{
				LOGGER.error("Making JWT Key Error ::: {}", e.getMessage());
			}
		}
		return key;
	}
	
	/**
	 * Claims로부터 key값에 매칭된 value를 불러옵니다.
	 * Object형으로 반환되기에, 사용할 변환 타입을 정확히 알고 있어야 합니다.
	 * 
	 * @param claims
	 * @param key
	 * @return
	 */
	public static Object getClaim(Claims claims, String key){
		return getClaim( (Map<String, Object>)claims, key );
	}
	
	/**
	 * Claims로부터 key값에 매칭된 value를 불러옵니다.
	 * Object형으로 반환되기에, 사용할 변환 타입을 정확히 알고 있어야 합니다.
	 * 
	 * @param claims
	 * @param key
	 * @return
	 */
	public static Object getClaim(Map<String, Object> claims, String key){
		return claims.get(key);
	}
	
	/**
	 * JWT의 위/변조 여부를 검증하고, Body부분을 Map형식으로 파싱하여 반환합니다.
	 * 
	 * @param token	JWT 문자열
	 * @param secret	복호화에 필요한 개인키
	 * @return JWT의 body()를 담은 Claims 반환
	 * @throws SignatureException	위/변조 확인시 예외 발생
	 */
	public static Claims getClaims(String token, String secret) throws Exception{
		Claims claims = new DefaultClaims();
		try{
			claims = Jwts.parser()
			.setSigningKey(secret.getBytes("UTF-8"))
			.parseClaimsJws(token).getBody();
			// OK, we can trust this JWT
			
		}catch(ExpiredJwtException eje){
	    //JWT를 생성할 때 지정한 유효기간 초과할 때.
	        LOGGER.debug("JWT 유효기간 초과");
	        throw new RuntimeException("JWT 유효기간 초과");
        }catch(UnsupportedJwtException uje){
        //예상하는 형식과 일치하지 않는 특정 형식이나 구성의 JWT일 때
        	LOGGER.debug("JWT 형식 불일치");
        	throw new RuntimeException("JWT 형식 불일치");
        }catch(MalformedJwtException mje){
        //JWT가 올바르게 구성되지 않았을 때
        	LOGGER.debug("잘못된 JWT 구성");
        	throw new RuntimeException("잘못된 JWT 구성");
        }catch(SignatureException se){
        //JWT의 기존 서명을 확인하지 못했을 때
        	LOGGER.debug("JWT 서명 확인 불가");
        	throw new RuntimeException("JWT 서명 확인 불가");
        }catch(IllegalArgumentException iae){
        	LOGGER.debug("JWT IllegalArgumentException");
        	throw new RuntimeException("JWT IllegalArgumentException");
        }catch (Exception e) {
        	LOGGER.debug("JWT 검증 중, 알 수 없는 오류 발생 {}", e);
        	throw new RuntimeException("JWT 검증 중, 알 수 없는 오류 발생 {}", e);
        }
		
		return claims;
	}
}
