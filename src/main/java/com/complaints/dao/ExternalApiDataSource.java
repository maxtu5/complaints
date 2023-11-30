package com.complaints.dao;

import java.util.Optional;
import java.util.UUID;

public interface ExternalApiDataSource<T> {
    Optional<T> findById(UUID id);
}
