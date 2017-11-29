import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;

/**
 * @author Administrator.
 */
public class test {



    public static void main(String[] args){


    String s = "123123";
        System.out.println(new Md5Hash(s).toString());


    }
}
