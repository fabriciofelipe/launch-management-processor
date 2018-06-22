package com.launch.management.launchmanagementprocessor.infrastructure.repository;

import com.launch.management.launchmanagementprocessor.domain.Launch;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LaunchRepository extends MongoRepository<Launch, String> {


}



