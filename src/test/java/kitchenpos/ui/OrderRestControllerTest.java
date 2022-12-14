package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.OrderService;
import kitchenpos.domain.*;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("OrderRestController 테스트")
@WebMvcTest(OrderRestController.class)
public class OrderRestControllerTest {
    @Autowired
    protected MockMvc webMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    private OrderService orderService;

    private OrderTable 첫번째_주문_테이블;
    private OrderTable 두번째_주문_테이블;
    private Order 첫번째_주문;
    private Order 두번째_주문;
    private OrderLineItem 첫번째_주문_항목;
    private OrderLineItem 두번째_주문_항목;

    @BeforeEach
    public void setUp() {
        MenuGroup 한마리메뉴_메뉴그룹 = MenuGroup.of(1L, "한마리메뉴");
        Product 후라이드치킨_상품 = new Product(1L, "후라이드치킨", new Price(BigDecimal.valueOf(16_000L)));
        Product 콜라_상품 = new Product(2L, "콜라", new Price(BigDecimal.valueOf(2_000L)));
        Menu 후라이드치킨_메뉴 = Menu.of(1L, "후라이드치킨", 후라이드치킨_상품.getPrice().value(), 한마리메뉴_메뉴그룹.getId());
        Menu 콜라_메뉴 = Menu.of(2L, "콜라", 콜라_상품.getPrice().value(), 한마리메뉴_메뉴그룹.getId());

        첫번째_주문_테이블 = OrderTable.of(1L, null, 4, false);
        두번째_주문_테이블 = OrderTable.of(2L, null, 2, false);
        첫번째_주문 = Order.of(1L, 첫번째_주문_테이블.getId(), null, null, null);
        두번째_주문 = Order.of(2L, 두번째_주문_테이블.getId(), null, null, null);
        첫번째_주문_항목 = OrderLineItem.of(1L, 첫번째_주문.getId(), 후라이드치킨_메뉴.getId(), 1);
        두번째_주문_항목 = OrderLineItem.of(2L, 첫번째_주문.getId(), 콜라_메뉴.getId(), 1);

        첫번째_주문.setOrderLineItems(Arrays.asList(첫번째_주문_항목, 두번째_주문_항목));
    }

    @DisplayName("주문 등록에 실패한다.")
    @Test
    void 주문_등록에_실패한다() throws Exception {
        given(orderService.create(any(Order.class))).willThrow(IllegalArgumentException.class);

        webMvc.perform(post("/api/orders")
                        .content(objectMapper.writeValueAsString(첫번째_주문))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("주문 등록에 성공한다.")
    @Test
    void 주문_등록에_성공한다() throws Exception {
        given(orderService.create(any(Order.class))).willReturn(첫번째_주문);

        webMvc.perform(post("/api/orders")
                        .content(objectMapper.writeValueAsString(첫번째_주문))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(첫번째_주문.getId().intValue())))
                .andExpect(jsonPath("$.orderTableId", is(첫번째_주문_테이블.getId().intValue())))
                .andExpect(jsonPath("$.orderLineItems", hasSize(2)));
    }

    @DisplayName("주문 목록 조회에 성공한다.")
    @Test
    void 주문_목록_조회에_성공한다() throws Exception {
        given(orderService.list()).willReturn(Arrays.asList(첫번째_주문, 두번째_주문));

        webMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @DisplayName("주문 상태 변경에 실패한다.")
    @Test
    void 주문_상태_변경에_실패한다() throws Exception {
        Order 상태_변경_주문 = new Order();
        상태_변경_주문.setId(첫번째_주문.getId());
        상태_변경_주문.setOrderStatus(OrderStatus.MEAL.name());

        given(orderService.changeOrderStatus(anyLong(), any(Order.class))).willThrow(IllegalArgumentException.class);

        webMvc.perform(put("/api/orders/" + 첫번째_주문.getId() + "/order-status")
                        .content(objectMapper.writeValueAsString(상태_변경_주문))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("주문 상태 변경에 성공한다.")
    @Test
    void 주문_상태_변경에_성공한다() throws Exception {
        Order 상태_변경_주문 = new Order();
        상태_변경_주문.setId(첫번째_주문.getId());
        상태_변경_주문.setOrderStatus(OrderStatus.MEAL.name());

        given(orderService.changeOrderStatus(anyLong(), any(Order.class))).willReturn(상태_변경_주문);

        webMvc.perform(put("/api/orders/" + 첫번째_주문.getId() + "/order-status")
                        .content(objectMapper.writeValueAsString(상태_변경_주문))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(상태_변경_주문.getId().intValue())))
                .andExpect(jsonPath("$.orderStatus", is(OrderStatus.MEAL.name())));
    }
}
