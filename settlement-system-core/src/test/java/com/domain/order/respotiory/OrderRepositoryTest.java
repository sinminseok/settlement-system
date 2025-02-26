package com.domain.order.respotiory;

import com.config.TestConfig;
import com.domain.order.entity.Order;
import com.domain.order.repository.OrderRepository;
import com.domain.shop.entity.Shop;
import com.domain.shop.repository.ShopRepository;
import com.domain.user.entity.User;
import com.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

import static com.generator.OrderGenerator.generateOrder;
import static com.generator.ShopGenerator.generateShop;
import static com.generator.UserGenerator.generateUser;

@DataJpaTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Test
    void 주문_정보를_페이징_조회한다(){
        //given
        User user = userRepository.save(generateUser());
        Shop shop = shopRepository.save(generateShop(user));

        for(int i=0; i<20; i++){
            orderRepository.save(generateOrder(shop));
        }
        //when
        Pageable pageable = PageRequest.of(1, 10, Sort.by(Sort.Direction.DESC, "startDateTime"));
        List<Order> byShopIdAndPage = orderRepository.findByShopIdAndPage(shop.getId(), pageable);

        //then
        Assertions.assertThat(byShopIdAndPage.size()).isEqualTo(10);
    }

    @Test
    void 기간에_포함된_주문_정보를_페이징_조회한다(){
       //given
        User user = userRepository.save(generateUser());
        Shop shop = shopRepository.save(generateShop(user));

       //when
        for(int i=0; i<15; i++){
            orderRepository.save(generateOrder(shop));
        }
        Pageable pageable = PageRequest.of(1, 10, Sort.by(Sort.Direction.DESC, "startDateTime"));
        List<Order> byShopIdAndPeriod = orderRepository.findByShopIdAndPeriod(shop.getId(), pageable, LocalDate.now(), LocalDate.now().plusDays(1));
        //then

        Assertions.assertThat(byShopIdAndPeriod.size()).isEqualTo(5);
    }

}
