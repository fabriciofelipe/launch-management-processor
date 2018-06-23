package com.launch.management.launchmanagementprocessor.application.stream;

import com.launch.management.launchmanagementprocessor.domain.Launch;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CashFlowPublish {

    private final Channels channels;

    public Optional<Launch> send(final Launch launch){
        final Message msg = MessageBuilder.withPayload(launch).build();
        channels.outPutFluxoCaixa().send(msg);
        return Optional.ofNullable(launch);
    }

}
