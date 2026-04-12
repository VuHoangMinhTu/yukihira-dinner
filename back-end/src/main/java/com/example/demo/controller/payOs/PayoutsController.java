package com.example.demo.controller.payOs;

import com.example.demo.dto.AppResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.payos.PayOS;
import vn.payos.core.Page;
import vn.payos.model.v1.payouts.GetPayoutListParams;
import vn.payos.model.v1.payouts.Payout;
import vn.payos.model.v1.payouts.PayoutApprovalState;
import vn.payos.model.v1.payouts.PayoutRequests;
import vn.payos.model.v1.payouts.batch.PayoutBatchItem;
import vn.payos.model.v1.payouts.batch.PayoutBatchRequest;
import vn.payos.model.v1.payoutsAccount.PayoutAccountInfo;

import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/payouts")
@RequiredArgsConstructor
public class PayoutsController {
    private final PayOS payOS;
    @PostMapping("/create")
    public AppResponse<Payout> create(@RequestBody PayoutRequests body) {
        try {
            if (body.getReferenceId() == null || body.getReferenceId().isEmpty()) {
                body.setReferenceId("payout_" + (System.currentTimeMillis() / 1000));
            }

            Payout payout = payOS.payouts().create(body);
            return AppResponse.<Payout>builder().data(payout).build();

        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.<Payout>builder().code(999).message("An error occurred in the system").data(null).build();
        }
    }

    @PostMapping("/batch/create")
    public AppResponse<Payout> createBatch(@RequestBody PayoutBatchRequest body) {
        try {
            if (body.getReferenceId() == null || body.getReferenceId().isEmpty()) {
                body.setReferenceId("payout_" + (System.currentTimeMillis() / 1000));
            }

            List<PayoutBatchItem> payoutsList = body.getPayouts();
            if (payoutsList == null) {
                return AppResponse.<Payout>builder().data(null).code(999).message("fail").build();
            }
            for (int i = 0; i < payoutsList.size(); i++) {
                PayoutBatchItem batchItem = payoutsList.get(i);
                if (batchItem.getReferenceId() == null) {
                    batchItem.setReferenceId("payout_" + (System.currentTimeMillis() / 1000) + "_" + i);
                }
            }

            Payout payout = payOS.payouts().batch().create(body);
            return AppResponse.<Payout>builder().data(payout).build();

        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.<Payout>builder().code(999).message("An error occurred in the system").data(null).build();
        }
    }

    @GetMapping("/{payoutId}")
    public AppResponse<Payout> retrieve(@PathVariable String payoutId) {
        try {
            Payout payout = payOS.payouts().get(payoutId);
            return AppResponse.<Payout>builder().data(payout).build();

        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.<Payout>builder().code(999).message("An error occurred in the system").data(null).build();
        }
    }

    @GetMapping("/list")
    public AppResponse<List<Payout>> retrieveList(
            @RequestParam(required = false) String referenceId,
            @RequestParam(required = false) String approvalState,
            @RequestParam(required = false) List<String> category,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer offset) {
        try {
            GetPayoutListParams.GetPayoutListParamsBuilder paramsBuilder =
                    GetPayoutListParams.builder()
                            .referenceId(referenceId)
                            .category(category)
                            .limit(limit)
                            .offset(offset);
            if (fromDate != null && !fromDate.isEmpty()) {
                paramsBuilder.fromDate(fromDate);
            }
            if (toDate != null && !toDate.isEmpty()) {
                paramsBuilder.toDate(toDate);
            }

            PayoutApprovalState parsedApprovalState = null;
            if (approvalState != null && !approvalState.isEmpty()) {
                try {
                    parsedApprovalState = PayoutApprovalState.valueOf(approvalState.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return AppResponse.<List<Payout>>builder().code(999).message("Invalid approval state").data(null).build();
                }
                paramsBuilder.approvalState(parsedApprovalState);
            }

            GetPayoutListParams params = paramsBuilder.build();

            List<Payout> data = new ArrayList<>();
            Page<Payout> page = payOS.payouts().list(params);
            page.autoPager().stream().forEach(data::add);
            return AppResponse.<List<Payout>>builder().data(data).build();

        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.<List<Payout>>builder().code(999).message("An error occurred in the system").data(null).build();
        }
    }

    @GetMapping("/balance")
    public AppResponse<PayoutAccountInfo> getAccountBalance() {
        try {
            PayoutAccountInfo accountInfo = payOS.payoutsAccount().balance();
            return AppResponse.<PayoutAccountInfo>builder().data(accountInfo).build();

        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.<PayoutAccountInfo>builder().code(999).message("An error occurred in the system").data(null).build();
        }
    }
}
