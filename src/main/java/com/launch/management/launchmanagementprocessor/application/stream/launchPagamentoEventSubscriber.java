package com.launch.management.launchmanagementprocessor.application.stream;

import com.launch.management.launchmanagementprocessor.application.service.LaunchService;
import com.launch.management.launchmanagementprocessor.domain.Launch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class launchPagamentoEventSubscriber {

    private final Channels channels;
    private final LaunchService launchService;

    @StreamListener(value = Channels.INPUT_PAGAMENTO)
    public void heandle(Launch launch){
        handler(launch);
    }

    private Launch handler(Launch event){
        log.info("launchPagamentoEventSubscriber lancamento de pagamento {}", event);
        launchService.create(event);
        return event;
    }
}
