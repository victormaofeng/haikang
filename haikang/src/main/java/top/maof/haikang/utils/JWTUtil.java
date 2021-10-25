package top.maof.haikang.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;
/**
 * JWT 工具类
 * 用来生成、验证和解析JWT
 *
 * @author 毛逢
 * @date 2021/9/16 9:42
 */
public class JWTUtil {

    private static final String SIGNATURE = "1315456eyJ0eXAiOiJKV1QiLCnk;l'.'';'llkjhugyghgjkgffl;l[ls[lgkojfvjovzklvfvoiusjfi0wir90824748r8g" +
            "wefsidjcmdlmcl;smzdlvmsl;zd/msdkij;ifuweur84r8774rihifjaopkgf5g464h41gf5h15g4b86s4g51r5g4gdhffhgjfjgfssfghfhfhvjgjgnvhng5s" +
            "JhbGciOiJIUzI1NiJ9.eyJzdHVkZW50SWQiOjIsImlkIjozLCJleHAiOjE1OTk5MDAzMTl9.9_rCXgTyOz0-2CzWonUPcBCHqWS3OzAtLp8f3a9kjihugyftcb" +
            "drseadgvhvfxea34479u09i09u89frdesa3yvlxmlfdogeig9eu8yer1254645646486868464444444448686786877777777777777777777777776865343" +
            "jnvmxclv,xlmblmglbdxkpokhokn;h,n;,hn;kdkpksgb;,b,zlvmkfmvkfjokofkpsdkfpskdfkodfadfnsjdnasdnadaspdkpakfofisdjifjsivfuvnfbvb" +
            "fhvisjdvodjcokdpcksdlmfwlmri4u23i403_8264595";

    // 默认有效时间 3小时
    public static Integer HOUR = 6;

    public static String getToken(Map<String, String> map) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, HOUR);
        JWTCreator.Builder builder = JWT.create();
        map.forEach((k, v) -> builder.withClaim(k, v));
        String token = builder.withExpiresAt(calendar.getTime()).sign(Algorithm.HMAC256(SIGNATURE));
        return token;
    }

    public static String getToken() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, HOUR);
        JWTCreator.Builder builder = JWT.create();
        String token = builder.withExpiresAt(calendar.getTime()).sign(Algorithm.HMAC256(SIGNATURE));
        return token;
    }

    public static boolean checkToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256(SIGNATURE)).build().verify(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static Map<String, Claim> parseToken(String token) {
        Map<String, Claim> claims = claimMap(token);
        return claims;
    }

    public static String parseToken(String token, String key) {
        Map<String, Claim> claims = claimMap(token);
        if (claims.containsKey(key)) {
            Claim claim = claims.get(key);
            return claim.asString();
        }
        return null;
    }

    private static Map<String, Claim> claimMap(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SIGNATURE);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            Map<String, Claim> claims = jwt.getClaims();
            return claims;
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer getUserId(String token) {
        return Integer.parseInt(parseToken(token, "id"));
    }
}
