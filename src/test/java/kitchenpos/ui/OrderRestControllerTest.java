package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.application.MenuGroupServiceTest.메뉴_그룹;
import static kitchenpos.application.MenuProductTest.메뉴_상품;
import static kitchenpos.application.MenuServiceTest.메뉴;
import static kitchenpos.application.OrderServiceTest.주문;
import static kitchenpos.application.TableServiceTest.두_명의_방문객;
import static kitchenpos.application.TableServiceTest.비어있지_않은_상태;
import static kitchenpos.application.TableServiceTest.주문_테이블;
import static kitchenpos.application.ProductTest.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("주문 ui 테스트")
class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderService orderService;

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() throws Exception {
        //given:
        final Menu 저장된_메뉴 = menuService.create(
                메뉴("자메이카 통다리 1인 세트",
                        BigDecimal.ONE,
                        menuGroupDao.save(메뉴_그룹("추천 메뉴")).getId(),
                        Arrays.asList(
                                메뉴_상품(productDao.save(상품("통다리", BigDecimal.ONE)).getId(), 5),
                                메뉴_상품(productDao.save(상품("콜라", BigDecimal.ONE)).getId(), 1))));

        final Order 주문 = 주문(
                orderTableDao.save(주문_테이블(두_명의_방문객, 비어있지_않은_상태)).getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L)));
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
                메뉴("자메이카 통다리 1인 세트",
                        BigDecimal.ONE,
                        menuGroupDao.save(메뉴_그룹("추천 메뉴")).getId(),
                        Arrays.asList(
                                메뉴_상품(productDao.save(상품("통다리", BigDecimal.ONE)).getId(), 5),
                                메뉴_상품(productDao.save(상품("콜라", BigDecimal.ONE)).getId(), 1))));

        final Order 저장된_주문 = orderService.create(주문(
                orderTableDao.save(주문_테이블(두_명의_방문객, 비어있지_않은_상태)).getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L))));
        //when:
        final List<Order> 주문_목록 = Arrays.asList(mapper.readValue(
                mockMvc.perform(get("/api/orders")
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(), Order[].class));
        //then:
        assertThat(주문_목록).containsExactly(저장된_주문);
    }

    @DisplayName("주문 상태 변경 성공")
    @Test
    void 주문_상태_변경_성공() throws Exception {
        //given:
        final Menu 저장된_메뉴 = menuService.create(
                메뉴("자메이카 통다리 1인 세트",
                        BigDecimal.ONE,
                        menuGroupDao.save(메뉴_그룹("추천 메뉴")).getId(),
                        Arrays.asList(
                                메뉴_상품(productDao.save(상품("통다리", BigDecimal.ONE)).getId(), 5),
                                메뉴_상품(productDao.save(상품("콜라", BigDecimal.ONE)).getId(), 1))));

        final Order 저장된_주문 = orderService.create(주문(
                orderTableDao.save(주문_테이블(두_명의_방문객, 비어있지_않은_상태)).getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L))));

        final Order 식사_상태_주문 = 저장된_주문.changeStatus(OrderStatus.MEAL);
        //when:
        final Order 상태_변경된_주문 = mapper.readValue(
                mockMvc.perform(put("/api/orders/{orderId}/order-status", 식사_상태_주문.getId())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(식사_상태_주문)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(), Order.class);
        //then:
        assertThat(상태_변경된_주문.isStatus(OrderStatus.MEAL)).isTrue();
    }
}
