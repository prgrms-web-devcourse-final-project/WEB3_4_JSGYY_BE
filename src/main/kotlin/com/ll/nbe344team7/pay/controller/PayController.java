package com.ll.nbe344team7.pay.controller;

import com.ll.nbe344team7.pay.dto.DepositDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author shjung
 * @since 25. 3. 24.
 */
@RestController
@RequestMapping("api/pay/deposit")
public class PayController {

    @PostMapping
    public ResponseEntity<?> depositAccount(@RequestBody DepositDTO depositDTO){
        return null;
    }
}
