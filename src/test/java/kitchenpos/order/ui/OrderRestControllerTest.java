package kitchenpos.order.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.application.MenuService;
import kitchenpos.order.application.OrderService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProductBag;
import kitchenpos.product.domain.Name;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemBag;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.menu.application.MenuGroupServiceTest.메뉴_그룹;
import static kitchenpos.menu.application.MenuGroupServiceTest.메뉴_그룹_추천_메뉴;
import static kitchenpos.product.domain.ProductTest.상품_콜라;
import static kitchenpos.product.domain.ProductTest.상품_통다리;
import static kitchenpos.table.application.TableServiceTest.주문_테이블;
import static kitchenpos.menu.domain.MenuProductTest.메뉴_상품;
import static kitchenpos.menu.domain.MenuTest.메뉴;
import static kitchenpos.order.domain.OrderTableTest.두_명의_방문객;
import static kitchenpos.order.domain.OrderTableTest.비어있지_않은_상태;
import static kitchenpos.order.domain.OrderTest.식사_상태;
import static kitchenpos.order.domain.OrderTest.조리_상태;
import static kitchenpos.order.domain.OrderTest.주문;
import static kitchenpos.product.domain.ProductTest.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("주문 ui 테스트")
class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderService orderService;

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() throws Exception {
        //given:
        final Menu 저장된_메뉴 = menuService.create(
                메뉴(Name.from("자메이카 통다리 1인 세트"),
                        Price.from(BigDecimal.ONE),
                        menuGroupRepository.save(메뉴_그룹_추천_메뉴()).getId(),
                        MenuProductBag.from(Arrays.asList(
                                메뉴_상품(productRepository.save(상품_통다리()), 5),
                                메뉴_상품(productRepository.save(상품_콜라()), 1)))));

        final Order 주문 = 주문(
                orderTableRepository.save(주문_테이블(두_명의_방문객, 비어있지_않은_상태)),
                조리_상태,
                LocalDateTime.now(),
                OrderLineItemBag.from(Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L))));
        //when:
        final Order 저장된_주문 = mapper.readValue(
                mockMvc.perform(post("/api/orders")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(주문)))
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andReturn().getResponse().getContentAsString(), Order.class);
        //then:
        assertThat(저장된_주문).isEqualTo(주문);
    }

    @DisplayName("목록 조회 성공")
    @Test
    void 목록_조회_성공() throws Exception {
        //given:
        final Menu 저장된_메뉴 = menuService.create(
                메뉴(Name.from("자메이카 통다리 1인 세트"),
                        Price.from(BigDecimal.ONE),
                        menuGroupRepository.save(메뉴_그룹_추천_메뉴()).getId(),
                        MenuProductBag.from(Arrays.asList(
                                메뉴_상품(productRepository.save(상품_통다리()), 5),
                                메뉴_상품(productRepository.save(상품_콜라()), 1)))));

        final Order 저장된_주문 = orderService.create(주문(
                orderTableRepository.save(주문_테이블(두_명의_방문객, 비어있지_않은_상태)),
                조리_상태,
                LocalDateTime.now(),
                OrderLineItemBag.from(Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L)))));
        //when:
        final List<Order> 주문_목록 = Arrays.asList(mapper.readValue(
                mockMvc.perform(get("/api/orders")
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(), Order[].class));
        //then:
        assertThat(주문_목록).hasSize(1);
    }

    @DisplayName("주문 상태 변경 성공")
    @Test
    void 주문_상태_변경_성공() throws Exception {
        //given:
        final Menu 저장된_메뉴 = menuService.create(
                메뉴(Name.from("자메이카 통다리 1인 세트"),
                        Price.from(BigDecimal.ONE),
                        menuGroupRepository.save(메뉴_그룹_추천_메뉴()).getId(),
                        MenuProductBag.from(Arrays.asList(
                                메뉴_상품(productRepository.save(상품_통다리()), 5),
                                메뉴_상품(productRepository.save(상품_콜라()), 1)))));

        final Order 저장된_주문 = orderService.create(주문(
                orderTableRepository.save(주문_테이블(두_명의_방문객, 비어있지_않은_상태)),
                조리_상태,
                LocalDateTime.now(),
                OrderLineItemBag.from(Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L)))));

        저장된_주문.changeStatus(식사_상태);
        //when:
        final Order 상태_변경된_주문 = mapper.readValue(
                mockMvc.perform(put("/api/orders/{orderId}/order-status", 저장된_주문.getId())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(저장된_주문)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(), Order.class);
        //then:
        assertThat(상태_변경된_주문.isStatus(OrderStatus.MEAL)).isTrue();
    }
}
