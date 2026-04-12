package com.example.demo.service.impl;

import com.example.demo.dto.request.CreatePaymentLinkRequestBody;
import com.example.demo.entity.Food;
import com.example.demo.entity.Orders;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Users;
import com.example.demo.exception.UnauthorizedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.FoodRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.OrderService;
import com.example.demo.utils.GetCurrentUser;
import vn.payos.PayOS;
import vn.payos.core.FileDownloadResponse;
import vn.payos.exception.APIException;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.v2.paymentRequests.PaymentLink;
import vn.payos.model.v2.paymentRequests.PaymentLinkItem;
import vn.payos.model.webhooks.ConfirmWebhookResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final PayOS payOS;
    private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    @Override
    public CreatePaymentLinkResponse createPaymentLink(CreatePaymentLinkRequestBody RequestBody) {
       Users users = findUserByUsername();
       if(users == null){
           throw new UnauthorizedException("Người dùng chưa đăng nhập");
       }

        long finalPrice = 0;
        List<PaymentLinkItem> listItemPaymentLink = new ArrayList<>();
        Orders orders = new Orders();
        List<OrderItem> orderItems = orders.getOrderItems();
        orders.setStatus("PENDING");
        orders.setUsers(users);
        orders.setAddress(RequestBody.getAddress());
        for(CreatePaymentLinkRequestBody.FoodOrder foodOrder : RequestBody.getFoodOrderList()) {
            final String productName = foodOrder.getProductName();
            final long price = convertPriceFromDoubleToLong(foodOrder.getPrice());
            finalPrice = finalPrice + price;
            PaymentLinkItem item = PaymentLinkItem.builder().name(productName).quantity(1).price(price).build();
            listItemPaymentLink.add(item);
            OrderItem orderItem = new OrderItem();
            Food foodEntity = foodRepository.findById(foodOrder.getProductId()).get();
            orderItem.setFood(foodEntity);
            orderItem.setImageUrl(foodOrder.getImageUrl());
            orderItem.setQuantity(foodOrder.getQuantity());
            orderItem.setOrders(orders);
            orderItems.add(orderItem);
        }
        orders.setTotalPrice(finalPrice+ 0.0);
        orderRepository.save(orders);

        long orderCode = orders.getId();

        final String returnUrl = RequestBody.getReturnUrl();
        final String cancelUrl = RequestBody.getCancelUrl();
        CreatePaymentLinkRequest paymentData =
                CreatePaymentLinkRequest.builder()
                        .orderCode(orderCode)
                        .description("Món ăn bạn đã đặt")
                        .amount(finalPrice)
                        .items(listItemPaymentLink)
                        .returnUrl(returnUrl)
                        .cancelUrl(cancelUrl)
                        .build();

        CreatePaymentLinkResponse data = payOS.paymentRequests().create(paymentData);
        return data;
    }

    @Override
    public ResponseEntity<?> downloadInvoice(long orderId, String invoiceId) {
        try {
            FileDownloadResponse invoiceFile =
                    payOS.paymentRequests().invoices().download(invoiceId, orderId);

            if (invoiceFile == null || invoiceFile.getData() == null) {
                return ResponseEntity.status(404).body("invoice not found or empty");
            }

            ByteArrayResource resource = new ByteArrayResource(invoiceFile.getData());

            HttpHeaders headers = new HttpHeaders();
            String contentType =
                    invoiceFile.getContentType() == null
                            ? MediaType.APPLICATION_PDF_VALUE
                            : invoiceFile.getContentType();
            headers.set(HttpHeaders.CONTENT_TYPE, contentType);
            headers.set(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + invoiceFile.getFilename() + "\"");
            if (invoiceFile.getSize() != null) {
                headers.setContentLength(invoiceFile.getSize());
            }

            return ResponseEntity.ok().headers(headers).body(resource);
        } catch (APIException e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body("An error occurred in the system");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred in the system");
        }
    }

    @Override
    public ConfirmWebhookResponse confirmWebhook(Map<String, String> requestBody) {
        return null;
    }

    @Override
    public PaymentLink cancelOrder(Long orderId) {
        PaymentLink paymentLink = payOS.paymentRequests().cancel(orderId);
        Orders orders = orderRepository.findById(orderId).get();
        orders.setStatus("CANCELLED");
        orderRepository.save(orders);
        return paymentLink;
    }

    private long convertPriceFromDoubleToLong(double price) {
        return (Math.round(price));
    }
    private Users findUserByUsername() {
        String username = GetCurrentUser.getCurrentUsername();
        return userRepository.findByUsername(username).orElse(null);
    }
}
