package com.example.demo.repository;

import com.example.demo.entity.DiningTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface DiningTableRepository extends JpaRepository<DiningTable, Long> {
    @Query(
            """
         SELECT count(r.id) from DiningTable r where r.tableStatus = 'RESERVED'
         and r.expireTime > :expireTime and r.arrivalTime < :timeArrival
"""
    )
//    Để tạm kiểu trả về là int xem có lỗi ko, nếu lỗi thì chuyển sang Long
    int countTotalReservedTables(@Param("timeArrival") LocalDateTime timeArrival, @Param("expireTime") LocalDateTime expireTime);
    @Query(
            """
     update DiningTable d set d.tableStatus = :status where d.id = :id
"""
    )
    int updateDiningTableStatus(@Param("id") Long id, @Param("status") String status);
    DiningTable findById(long id);
}
