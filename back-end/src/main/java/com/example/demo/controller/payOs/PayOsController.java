package com.example.demo.controller.payOs;


import java.util.Map;

import com.example.demo.dto.AppResponse;
import com.example.demo.dto.request.CreatePaymentLinkRequestBody;
import com.example.demo.entity.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;
import vn.payos.PayOS;
import vn.payos.core.FileDownloadResponse;
import vn.payos.exception.APIException;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.v2.paymentRequests.PaymentLink;
import vn.payos.model.v2.paymentRequests.invoices.InvoicesInfo;
import vn.payos.model.webhooks.ConfirmWebhookResponse;
import vn.payos.model.webhooks.WebhookData;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class PayOsController {
    private final PayOS payOS;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    @PostMapping(path = "/create")
    public AppResponse<CreatePaymentLinkResponse> createPaymentLink(
            @RequestBody CreatePaymentLinkRequestBody RequestBody) {
        try {
            CreatePaymentLinkResponse data = orderService.createPaymentLink(RequestBody);
            return AppResponse.<CreatePaymentLinkResponse>builder().data(data).build();
        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.<CreatePaymentLinkResponse>builder().code(999).message("An error occurred in the system").data(null).build();
        }
    }

    @GetMapping(path = "/{orderId}")
    public AppResponse<PaymentLink> getOrderById(@PathVariable("orderId") long orderId) {
        try {
            PaymentLink order = payOS.paymentRequests().get(orderId);
            return AppResponse.<PaymentLink>builder().data(order).build();
        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.<PaymentLink>builder().code(999).message("An error occurred in the system").data(null).build();
        }
    }

    @PutMapping(path = "/{orderId}")
    public AppResponse<PaymentLink> cancelOrder(@PathVariable("orderId") long orderId) {
        try {
            PaymentLink order = orderService.cancelOrder(orderId);
            return AppResponse.<PaymentLink>builder().data(order).message("Hủy đơn thành công").build();
        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.<PaymentLink>builder().code(999).message("An error occurred in the system").data(null).build();
        }
    }

    @PostMapping(path = "/confirm-webhook")
    public AppResponse<ConfirmWebhookResponse> confirmWebhook(
            @RequestBody Map<String, String> requestBody) {
        try {
            ConfirmWebhookResponse result = payOS.webhooks().confirm(requestBody.get("webhookUrl"));
            return AppResponse.<ConfirmWebhookResponse>builder().data(result).build();
        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.<ConfirmWebhookResponse>builder().code(999).message("An error occurred in the system").data(null).build();
        }
    }

    @GetMapping(path = "/{orderId}/invoices")
    public AppResponse<InvoicesInfo> retrieveInvoices(@PathVariable("orderId") long orderId) {
        try {
            InvoicesInfo invoicesInfo = payOS.paymentRequests().invoices().get(orderId);
            return AppResponse.<InvoicesInfo>builder().data(invoicesInfo).build();
        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.<InvoicesInfo>builder().code(999).message("An error occurred in the system").data(null).build();
        }
    }

    @GetMapping(path = "/{orderId}/invoices/{invoiceId}/download")
    public ResponseEntity<?> downloadInvoice(
            @PathVariable("orderId") long orderId, @PathVariable("invoiceId") String invoiceId) {
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
//    API này giuúp bắt được data webhook mà Payos trả ve sau khi Khách hàng thanh toán xong
    @PostMapping(path = "webhook/payos_transfer_handler")
    @Transactional
    public AppResponse<WebhookData> payosTransferHandler(@RequestBody Object body){
      try {
          WebhookData data = payOS.webhooks().verify(body);
          Orders orders = orderRepository.findById(data.getOrderCode()).get();
          orders.setStatus("COMPLETED");
          orderRepository.save(orders);
          return AppResponse.<WebhookData>builder().data(data).build();
      } catch (Exception e) {
          e.printStackTrace();
          return AppResponse.<WebhookData>builder().message("Webhook received but ignored").build();
      }
    }
}