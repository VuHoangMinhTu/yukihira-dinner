package com.example.demo.controller.payOs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.example.demo.dto.AppResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.payos.PayOS;
import vn.payos.model.webhooks.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class WebhookController {
    private final PayOS payOS;

    @PostMapping(path = "/payos_transfer_handler")
    public AppResponse<WebhookData> payosTransferHandler(@RequestBody Object body)
            throws JsonProcessingException, IllegalArgumentException {
        try {
            WebhookData data = payOS.webhooks().verify(body);
            System.out.println(data);
            return AppResponse.<WebhookData>builder().data(data).build();
        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.<WebhookData>builder().code(999).message("An error occurred in the system").data(null).build();
        }
    }
}