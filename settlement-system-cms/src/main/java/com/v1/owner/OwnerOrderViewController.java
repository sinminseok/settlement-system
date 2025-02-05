package com.v1.owner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/owner")
public class OwnerOrderViewController {

    @GetMapping("/owner_main")
    public String ownerMain() {
        return "owner/owner_main";
    }

}
