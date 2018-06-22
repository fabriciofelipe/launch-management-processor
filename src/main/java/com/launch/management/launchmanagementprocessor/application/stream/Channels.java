package com.launch.management.launchmanagementprocessor.application.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface Channels {

    String INPUT_PAGAMENTO = "inputPagamento";
    String INPUT_RECEBIMENTO = "inputRecebimento";

    @Input(Channels.INPUT_PAGAMENTO)
    SubscribableChannel inputPagamento();

    @Input(Channels.INPUT_RECEBIMENTO)
    SubscribableChannel inputRecebimento();
}
