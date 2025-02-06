package com.v1.shop;

import com.domain.shop.dto.ShopResponse;
import com.domain.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;


    @GetMapping("/shops")
    public String getAllShops(Model model) {
        List<ShopResponse> shops = shopService.findAll();
        model.addAttribute("shops", shops);
        return "shop/list";
    }
}
