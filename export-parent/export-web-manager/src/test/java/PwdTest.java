import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/applicationContext-*.xml"})

public class PwdTest {

    //加密
    @Test
    public void md5() {
        // 明文    算法     密文
        //  1      md5       c4ca4238a0b923820dcc509a6f75849b
        System.out.println(new Md5Hash("1").toString());
    }

    //加密加盐
    @Test
    public void md5Salt() {
        //用户名
        String username = "user1@export.com";
        //密码
        String password = "1";

        // 参数1：密码, 参数2：盐；把用户名作为盐
        Md5Hash encodePassword = new Md5Hash(password, username);
        //  5629ea0d67afa3ef99a5a5656a8222ea
        System.out.println("根据用户名作为盐，加密加盐后的结果：" + encodePassword);
    }
}
