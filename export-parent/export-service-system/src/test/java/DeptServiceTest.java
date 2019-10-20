import com.zmy.service.system.DeptService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/applicationContext*.xml")
public class DeptServiceTest {

    @Autowired
    private DeptService deptService;

    @Test
    public void  tt(){
        deptService.findAllDept("1");
    }

    @Test
    public void test(){
        int pageNum = 1;
        int pageSize = 5;

        deptService.findByPage("1", pageNum, pageSize);
    }
}
