package base.core.faceit;

import base.core.faceit.util.Scheduler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class JobBoardAppApplicationTests {
    //disable sync job for this test
    @MockBean
    private Scheduler scheduler;

    @Test
    void contextLoads() {
    }
}
