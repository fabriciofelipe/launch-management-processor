package com.launch.management.launchmanagementprocessor.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "launchs")
public class Launch {


    private String tipoLancamento;
    private String descricao;
    private String contaDestino;
    private String bancoDestino;
    private String tipoConta;
    private String cpfCnpjDestino;
    private BigDecimal valorLancamento;
    private BigDecimal encargos;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dataLacamento;



}
