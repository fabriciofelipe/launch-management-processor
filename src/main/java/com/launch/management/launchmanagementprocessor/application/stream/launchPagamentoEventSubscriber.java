package com.launch.management.launchmanagementprocessor.application.stream;

import com.launch.management.launchmanagementprocessor.domain.Launch;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class launchPagamentoEventSubscriber {

    private final Channels channels;

    @StreamListener(value = Channels.INPUT_PAGAMENTO)
    public void heandle(Flux <Launch> launch){
        launch.subscribe(this::handler);
    }

    private Flux<Launch> handler(Launch event){
        System.out.println("launchPagamentoEventSubscriber.handler" + event.toString());
        return Flux.just(event);
    }
}
