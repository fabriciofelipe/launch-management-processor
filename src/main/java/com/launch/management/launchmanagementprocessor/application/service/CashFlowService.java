package com.launch.management.launchmanagementprocessor.application.service;

import com.launch.management.launchmanagementprocessor.domain.Launch;
import com.launch.management.launchmanagementprocessor.infrastructure.repository.CashFlowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CashFlowService {

    private final CashFlowRepository cashFlowRepository;

    public void create(Launch launch){
        Optional<Launch> launchTransient = Optional.ofNullable(launch);
    }

}
