import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyTest {

    @Test
    public void test01(){
        SimpleDateFormat simpleFormatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String time = simpleFormatter.format(new Date());
        System.out.println(time);
    }
}
