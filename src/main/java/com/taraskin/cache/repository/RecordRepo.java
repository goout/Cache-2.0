package com.taraskin.cache.repository;

import com.taraskin.cache.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepo extends JpaRepository <Record, Long> {
}
