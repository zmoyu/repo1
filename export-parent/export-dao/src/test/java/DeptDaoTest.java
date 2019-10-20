import com.zmy.dao.system.DeptDao;
import com.zmy.domain.system.Dept;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/applicationContext-dao.xml")
public class DeptDaoTest {

    @Autowired
    private DeptDao deptDao;

    @Test
    public void findAllDept(){

        List<Dept> deptList = deptDao.findAllDept("1");
        System.out.println("deptList = " + deptList);
    }
}
