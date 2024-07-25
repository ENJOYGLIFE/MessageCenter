import com.hdu.message.manager.ManagerApplication;
import com.hdu.message.manager.service.TestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ManagerApplication.class)
public class SpringBootApplicationTests {

    @Autowired
    private TestService testService;

    @Test
    public void testStrategy() {
        testService.testStrategy();
    }
}
