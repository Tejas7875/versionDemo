package com.CurrencyConverter.jpa;

import com.CurrencyConverter.entity.CurrencyConversionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyConversionRecordRepository extends JpaRepository<CurrencyConversionRecord, Long> {
}
