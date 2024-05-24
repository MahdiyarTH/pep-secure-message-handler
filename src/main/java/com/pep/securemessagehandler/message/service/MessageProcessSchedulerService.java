package com.pep.securemessagehandler.message.service;

import com.pep.securemessagehandler.message.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageProcessSchedulerService {

    private final MessageService messageService;
    private ExecutorService threadPoolExecutor;

    @PostConstruct
    public void initiateThreadPool() {
        this.threadPoolExecutor = Executors.newFixedThreadPool(10);
    }

    //By using below code, method runs 1 hour after application starts
    @Scheduled(fixedRate = 60 * 60 * 1000)
    //By using below code, method runs at round hour, like 1 o'clock
    //@Scheduled(cron = "0 0 * * * *")
    public void processInfoMessages() {
        processMessages(messageService.getForProcess(Message.Type.INFO));
    }


    @Scheduled(fixedRate = 15 * 60 * 1000)
    //@Scheduled(cron = "0 */15 * * * *")
    public void processErrorMessages() {
        processMessages(messageService.getForProcess(Message.Type.ERROR));
    }

    @Scheduled(fixedRate = 2 * 60 * 60 * 1000)
    //@Scheduled(cron = "0 0 */2 * * *")
    public void processWarningMessages() {
        processMessages(messageService.getForProcess(Message.Type.WARNING));
    }

    private void processMessages(List<Message> messages) {
        List<CompletableFuture<Message>> futures = messages.stream()
                .map(message -> CompletableFuture.supplyAsync(
                        () -> {
                            message.setProcessed(true);
                            return message;
                        },
                        threadPoolExecutor)
                )
                .collect(Collectors.toList());

        List<Message> processedMessages = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        threadPoolExecutor.shutdown();

        messageService.saveAll(processedMessages);
    }

}
