package com.launch.management.launchmanagementprocessor.application.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface Channels {

    String INPUT_PAGAMENTO = "inputPagamento";
    String INPUT_RECEBIMENTO = "inputRecebimento";
    String OUTPUT_FUXO_CAIXA =  "outputFuxoCaixa";
    String INPUT_FUXO_CAIXA =  "inputFuxoCaixa";

    @Input(Channels.INPUT_PAGAMENTO)
    SubscribableChannel inputPagamento();

    @Input(Channels.INPUT_RECEBIMENTO)
    SubscribableChannel inputRecebimento();

    @Output(Channels.OUTPUT_FUXO_CAIXA)
    MessageChannel outPutFluxoCaixa();

    @Input(Channels.INPUT_FUXO_CAIXA)
    SubscribableChannel inputFluxoCaixa();
}
