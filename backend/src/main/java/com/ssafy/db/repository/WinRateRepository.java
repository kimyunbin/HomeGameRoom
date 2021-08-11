package com.ssafy.db.repository;

import com.ssafy.db.entity.WinRate;
import com.ssafy.api.response.WinRateMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WinRateRepository extends JpaRepository<WinRate,Long> {
    List<WinRateMapping> findByUserId(Long userId);
}
