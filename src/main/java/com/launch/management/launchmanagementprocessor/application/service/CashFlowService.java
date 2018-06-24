package com.launch.management.launchmanagementprocessor.application.service;

import com.launch.management.launchmanagementprocessor.domain.*;
import com.launch.management.launchmanagementprocessor.infrastructure.repository.CashFlowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CashFlowService {

    private static final String RECEBIMENTO = "recebimento";
    private final CashFlowRepository cashFlowRepository;

    public void create(Launch launch){
        Optional<Launch> launchTransient = Optional.ofNullable(launch);
        Optional<CashFlow> cashFlowRepo = cashFlowRepository.findByCpfCnpjAndData(launch.getCpfCnpjDestino(), launch.getDataLacamento());

        CashFlow cfTransiente = cashFlowRepo.map(cashFlow -> {
            CashFlow cf = updateCashFlow(launchTransient, cashFlow);
            return cf;
        }).orElse(insertCashFlow(launchTransient));

        cfTransiente.setTotal(totalDia(cfTransiente.getEntradas(), cfTransiente.getSaidas()));

        cfTransiente.setPosicaoDia(compareDiaAtualDiaAnterior(cfTransiente));

        cashFlowRepository.save(cfTransiente);
    }

    private CashFlow insertCashFlow(Optional<Launch> launchTransient) {
        CashFlow cashFlow = CashFlow.builder()
                .data(launchTransient.get().getDataLacamento())
                .cpfCnpj(launchTransient.get().getCpfCnpjDestino())
                .build();

        return cashFlowBuilder(launchTransient, cashFlow);
    }



    private CashFlow updateCashFlow(Optional<Launch> launchTransient, CashFlow cashFlow) {
        return cashFlowBuilder(launchTransient, cashFlow);
    }

    private CashFlow cashFlowBuilder(Optional<Launch> launchTransient, CashFlow cashFlow) {

        if (launchTransient.get().getTipoLancamento().equalsIgnoreCase(RECEBIMENTO)){
            addRecebimento(cashFlow, launchTransient);
        } else {
            addPagamento(cashFlow,launchTransient);
        }
        return addEncargo(cashFlow,launchTransient);
    }

    private CashFlow addRecebimento(CashFlow cashFlow , Optional<Launch> launchTransient){
        Optional<Entrada> entrada = launchTransient.map(this::parseToEntrada);
        Optional<List<Entrada>> entradaRepo = Optional.ofNullable(cashFlow.getEntradas());
        List<Entrada> nova = entradaRepo.map(ent ->{
            ent.add(entrada.get());
            return  ent;
        }).orElse(new ArrayList<>(Collections.singleton(entrada.get())));

        cashFlow.setEntradas(nova);
        return cashFlow;
    }

    private Entrada parseToEntrada(Launch launchTransient){
        return Entrada.builder()
                .data(launchTransient.getDataLacamento())
                .valor(launchTransient.getValorLancamento())
                .build();
    }

    private CashFlow addPagamento(CashFlow cashFlow , Optional<Launch> launchTransient){
        Optional<Saida> saida = launchTransient.map(this::parseToSaida);
        Optional<List<Saida>> saidaRepo = Optional.ofNullable(cashFlow.getSaidas());
        List<Saida> nova = saidaRepo.map(sai ->{
            sai.add(saida.get());
            return  sai;
        }).orElse(new ArrayList<>(Collections.singleton(saida.get())));

        cashFlow.setSaidas(nova);
        return cashFlow;
    }

    private Saida parseToSaida(Launch launchTransient){
        return Saida.builder()
                .data(launchTransient.getDataLacamento())
                .valor(launchTransient.getValorLancamento())
                .build();
    }

    private CashFlow addEncargo(CashFlow cashFlow , Optional<Launch> launchTransient){
        Optional<Encargo> encargo = launchTransient.map(this::parseToEncargo);
        Optional<List<Encargo>> encargoRepo = Optional.ofNullable(cashFlow.getEncargos());
        List<Encargo> nova = encargoRepo.map(enc ->{
            enc.add(encargo.get());
            return  enc;
        }).orElse(new ArrayList<>(Collections.singleton(encargo.get())));

        cashFlow.setEncargos(nova);
        return cashFlow;
    }

    private Encargo parseToEncargo(Launch launchTransient){
        return Encargo.builder()
                .data(launchTransient.getDataLacamento())
                .valor(launchTransient.getEncargos())
                .build();
    }

    private BigDecimal totalDia(List<Entrada> entradas, List<Saida> saidas){

        Double totalEntradas = 0d;
        Double totalSaidas = 0d;

        if (entradas != null){
            totalEntradas = entradas.stream().mapToDouble(entrada -> entrada.getValor().doubleValue()).sum();
        }
        if (saidas != null) {
            totalSaidas = saidas.stream().mapToDouble(entrada -> entrada.getValor().doubleValue()).sum();
        }
        return BigDecimal.valueOf(totalEntradas - totalSaidas);
    }

    private String compareDiaAtualDiaAnterior(CashFlow cashFlowDia){
        Optional<CashFlow> cashFlowDiaAnterior = cashFlowRepository.findByCpfCnpjAndData(cashFlowDia.getCpfCnpj(), cashFlowDia.getData().minusDays(1));

        BigDecimal diaAnterior = cashFlowDiaAnterior.map(total -> {
            BigDecimal ret = total.getTotal();
            return ret;
        }).orElse(BigDecimal.ZERO);

        if(diaAnterior.equals(BigDecimal.ZERO)){
            return "0 %";
        }

        //((V2-V1)/V1 × 100)
        BigDecimal posicaoDia = cashFlowDia.getTotal().subtract(diaAnterior)
                                                      .divide(diaAnterior)
                                                      .multiply(BigDecimal.valueOf(100));
        return posicaoDia.toString() + " %";
    }
}
