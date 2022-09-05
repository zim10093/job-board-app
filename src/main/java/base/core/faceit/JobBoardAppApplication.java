package base.core.faceit;

import base.core.faceit.util.Scheduler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class JobBoardAppApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(JobBoardAppApplication.class, args);
        Scheduler scheduler = (Scheduler) context.getBean("scheduler");
        scheduler.startSync();
    }
}
