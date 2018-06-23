package com.launch.management.launchmanagementprocessor.infrastructure.repository;

import com.launch.management.launchmanagementprocessor.domain.CashFlow;
import com.launch.management.launchmanagementprocessor.domain.Launch;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CashFlowRepository extends MongoRepository<CashFlow, String> {

    public Optional<CashFlow> findByCpfCnpj(String cpfCnpj);


}



