package com.launch.management.launchmanagementprocessor.application.stream;

import com.launch.management.launchmanagementprocessor.application.service.CashFlowService;
import com.launch.management.launchmanagementprocessor.application.service.LaunchService;
import com.launch.management.launchmanagementprocessor.domain.Launch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CashFlowEventSubscriber {

    private final CashFlowService cashFlowService;

    @StreamListener(value = Channels.INPUT_FUXO_CAIXA)
    public void heandle(Launch launch){
        handler(launch);
    }

    private Launch handler(Launch event){
        log.info("FluxoCaixaEventSubscriber para o envent {}", event);
        cashFlowService.create(event);
        return event;
    }
}
