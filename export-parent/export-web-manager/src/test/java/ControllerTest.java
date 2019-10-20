import com.zmy.domain.company.Company;
import com.zmy.service.company.CompanyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/applicationContext-*.xml",
        "classpath:spring/springmvc.xml"})
public class ControllerTest {
    @Autowired
    CompanyService companyService;

    @Test
    public void save(Company company) {
        companyService.save(company);
        companyService.update(company);
    }

}
