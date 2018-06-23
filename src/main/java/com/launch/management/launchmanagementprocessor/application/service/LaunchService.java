package com.launch.management.launchmanagementprocessor.application.service;

import com.launch.management.launchmanagementprocessor.application.stream.CashFlowPublish;
import com.launch.management.launchmanagementprocessor.domain.Launch;
import com.launch.management.launchmanagementprocessor.infrastructure.repository.LaunchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LaunchService {

    private final CashFlowPublish cashFlowPublish;

    private final LaunchRepository launchRepository;

    public Optional<Launch> create(Launch launch){
        Optional<Launch> launchTransient = Optional.ofNullable(launch);
        launchTransient.map(launchRepository::save).map(cashFlowPublish::send);
        return launchTransient;
    }

}
