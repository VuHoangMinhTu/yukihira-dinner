package com.example.demo.service;
import com.example.demo.dto.request.CreatePaymentLinkRequestBody;
import org.springframework.http.ResponseEntity;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.v2.paymentRequests.PaymentLink;
import vn.payos.model.webhooks.ConfirmWebhookResponse;

import java.util.Map;

public interface OrderService {
    CreatePaymentLinkResponse createPaymentLink(CreatePaymentLinkRequestBody RequestBody);
    ResponseEntity<?> downloadInvoice(long orderId, String invoiceId);
    ConfirmWebhookResponse confirmWebhook(Map<String, String> requestBody);
    PaymentLink cancelOrder(Long orderId);
}
