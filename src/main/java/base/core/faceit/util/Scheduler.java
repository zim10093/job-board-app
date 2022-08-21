package base.core.faceit.util;

import base.core.faceit.exception.SyncException;
import base.core.faceit.service.SyncExternalJobService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Scheduler {
    private final SyncExternalJobService syncExternalJobService;
    private ExecutorService executorService;

    {
        executorService = Executors.newSingleThreadExecutor();
    }

    @PostConstruct
    @Scheduled(cron = "25 * * * * ?")
    public void startSync() {
        try {
            executorService.submit(syncExternalJobService);
        } catch (Exception e) {
            throw new SyncException("Can`t sync jobs", e);
        }
    }
}
