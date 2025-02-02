package com.v1;

import com.domain.shop.dto.ShopResponse;
import com.domain.shop.service.ShopService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping("/shops")
    public String getAllShops(Model model) {
        // Shop 정보를 조회하여 모델에 추가
        List<ShopResponse> shops = shopService.findAll();
        model.addAttribute("shops", shops);
        return "shop/list"; // Thymeleaf 템플릿 파일 이름
    }
}
