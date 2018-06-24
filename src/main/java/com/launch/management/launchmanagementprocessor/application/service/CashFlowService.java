package com.launch.management.launchmanagementprocessor.application.service;

import com.launch.management.launchmanagementprocessor.domain.*;
import com.launch.management.launchmanagementprocessor.infrastructure.repository.CashFlowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.DoubleStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class CashFlowService {

    private static final String RECEBIMENTO = "recebimento";
    private final CashFlowRepository cashFlowRepository;

    public void create(Launch launch){
        Optional<Launch> launchTransient = Optional.ofNullable(launch);
        Optional<CashFlow> cashFlowRepo = cashFlowRepository.findByCpfCnpj(launch.getCpfCnpjDestino());

        CashFlow cfTransiente = cashFlowRepo.map(cashFlow -> {
            CashFlow cf = updateCashFlow(launch, launchTransient, cashFlow);
            return cf;
        }).orElse(insertCashFlow(launch,launchTransient));

        cfTransiente.setTotal(totalDia(cfTransiente.getEntradas(), cfTransiente.getSaidas()));

        cashFlowRepository.save(cfTransiente);
    }

    private CashFlow insertCashFlow(Launch launch, Optional<Launch> launchTransient) {
        CashFlow cashFlow = CashFlow.builder()
                .data(LocalDate.now())
                .cpfCnpj(launch.getCpfCnpjDestino())
                .build();

        return cashFlowBuilder(launch, launchTransient, cashFlow);
    }



    private CashFlow updateCashFlow(Launch launch, Optional<Launch> launchTransient, CashFlow cashFlow) {
        return cashFlowBuilder(launch, launchTransient, cashFlow);
    }

    private CashFlow cashFlowBuilder(Launch launch, Optional<Launch> launchTransient, CashFlow cashFlow) {

        if (launch.getTipoLancamento().equalsIgnoreCase(RECEBIMENTO)){
            addLaunchRecebimento(cashFlow, launchTransient);
        } else {
            addLaunchPagamento(cashFlow,launchTransient);
        }
        return addEncargo(cashFlow,launchTransient);
    }

    private CashFlow addLaunchRecebimento(CashFlow cashFlow , Optional<Launch> launch){
        Optional<Entrada> entrada = launch.map(this::parseToEntrada);
        Optional<List<Entrada>> entradaRepo = Optional.ofNullable(cashFlow.getEntradas());
        List<Entrada> nova = entradaRepo.map(ent ->{
            ent.add(entrada.get());
            return  ent;
        }).orElse(new ArrayList<>(Collections.singleton(entrada.get())));

        cashFlow.setEntradas(nova);
        return cashFlow;
    }

    private Entrada parseToEntrada(Launch launch){
        return Entrada.builder()
                .data(launch.getDataLacamento())
                .valor(launch.getValorLancamento())
                .build();
    }

    private CashFlow addLaunchPagamento(CashFlow cashFlow , Optional<Launch> launch){
        Optional<Saida> saida = launch.map(this::parseToSaida);
        Optional<List<Saida>> saidaRepo = Optional.ofNullable(cashFlow.getSaidas());
        List<Saida> nova = saidaRepo.map(sai ->{
            sai.add(saida.get());
            return  sai;
        }).orElse(new ArrayList<>(Collections.singleton(saida.get())));

        cashFlow.setSaidas(nova);
        return cashFlow;
    }

    private Saida parseToSaida(Launch launch){
        return Saida.builder()
                .data(launch.getDataLacamento())
                .valor(launch.getValorLancamento())
                .build();
    }

    private CashFlow addEncargo(CashFlow cashFlow , Optional<Launch> launch){
        Optional<Encargo> encargo = launch.map(this::parseToEncargo);
        Optional<List<Encargo>> encargoRepo = Optional.ofNullable(cashFlow.getEncargos());
        List<Encargo> nova = encargoRepo.map(enc ->{
            enc.add(encargo.get());
            return  enc;
        }).orElse(new ArrayList<>(Collections.singleton(encargo.get())));

        cashFlow.setEncargos(nova);
        return cashFlow;
    }

    private Encargo parseToEncargo(Launch launch){
        return Encargo.builder()
                .data(launch.getDataLacamento())
                .valor(launch.getEncargos())
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
}
