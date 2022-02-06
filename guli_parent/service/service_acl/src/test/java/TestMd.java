import com.atguigu.commonutils.Md5Utils;
import org.junit.Test;

public class TestMd {

  @Test
  public void test() {
    System.out.println(Md5Utils.encrypt("123123"));
  }
}
