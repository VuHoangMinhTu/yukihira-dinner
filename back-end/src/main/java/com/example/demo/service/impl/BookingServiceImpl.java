package com.example.demo.service.impl;

import com.example.demo.constant.ReservationStatus;
import com.example.demo.constant.TableStatus;
import com.example.demo.dto.request.BookingRequestDTO;
import com.example.demo.entity.DiningTable;
import com.example.demo.entity.Reservations;
import com.example.demo.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.repository.DiningTableRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BookingService;
import com.example.demo.utils.GetCurrentUser;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final ReservationRepository reservationRepository;
    private final DiningTableRepository diningTableRepository;
    private static final int MAX_TABLES = 30;
    private final UserRepository userRepository;
    @Override
    @Transactional
    public String createBooking(BookingRequestDTO request) {
        LocalDateTime now = LocalDateTime.now();
        if(request.getEmail() == null || request.getEmail().isEmpty()
        || request.getNameCustomer() == null || request.getNameCustomer().isEmpty()
        || request.getPhoneNumber() == null || request.getPhoneNumber().isEmpty()
        || request.getQuantityGuest() == null || request.getQuantityGuest() == 0
        || request.getTimeArrival() == null
        ){
        return "Quý khách chưa nhập đầy đủ thống tin. Vui lòng kiểm tra lai.";
        }
        if(request.getTimeArrival().isBefore(now)){
            return "Thời gian đặt không phù hợp. Vui lòng kiểm tra lai.";
        }
        if(diningTableRepository.countTotalReservedTables(request.getTimeArrival(), request.getTimeArrival().plusHours(2)) >= MAX_TABLES){
            return "Nhà hàng đã đủ người ăn rồi. Mong quý khách đặt vào khung giờ sau";
        }

        String userNameCurrent = GetCurrentUser.getCurrentUsername();
        Users currentUsers = userRepository.findByUsername(userNameCurrent).get();

        DiningTable diningTable = diningTableRepository.findById(request.getDiningTableId()).get();
        Users hasUsersBookingTable = diningTable.getUsers();
        if(hasUsersBookingTable != null ){
            if(hasUsersBookingTable.getUsername().equals(currentUsers.getUsername())){
                return "Quý khách đã đặt bàn này rồi";
            }else {
                if(request.getTimeArrival().isBefore(diningTable.getExpireTime())){
                    return "Bàn này đã có người khác đặt rồi, vui lòng chọn bàn khác";
                }

            }
        }
        Reservations reservations = new Reservations();
        reservations.setPhoneNumber(request.getPhoneNumber());
        reservations.setNote(request.getNote());
        reservations.setNumberGuest(request.getQuantityGuest());
        reservations.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservations);


        diningTable.setArrivalTime(request.getTimeArrival());
        diningTable.setExpireTime(request.getTimeArrival().plusHours(2));
        diningTable.setUsers(currentUsers);
        diningTable.setTableStatus(TableStatus.RESERVED);
        diningTableRepository.save(diningTable);

        return "Quý khách đã đặt bàn thành công. Quý khách vui lòng đến đúng thời gian đã đặt. Nếu sau 2 tiếng quý khách không tới, hệ thống sẽ tự động hủy bàn";
    }
}
