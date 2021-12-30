package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.OrderService;
import kitchenpos.domain.*;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderLineItemResponse;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.ordertable.OrderTableResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    @MockBean
    private OrderService orderService;

    private Menu 메뉴;
    private MenuGroup 메뉴그룹;
    private Order 주문;
    private TableGroup 테이블그룹;
    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private OrderLineItem 주문상품;

    private OrderLineItemRequest 주문상품_요청;
    private OrderRequest 주문_요청;
    private OrderRequest 주문완료_요청;
    private OrderResponse 주문_응답;
    private OrderTableResponse 주문테이블_응답;
    private OrderLineItemResponse 주문상품_응답;
    private OrderResponse 주문완료_응답;

    @BeforeEach
    void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        메뉴그룹 = MenuGroup.of(1L, "메뉴그룹");
        메뉴 = Menu.of(1L, "메뉴", 15000, 메뉴그룹);
        주문테이블1 = OrderTable.of(1L, 1, false);
        주문테이블2 = OrderTable.of(2L, 1, false);
        테이블그룹 = TableGroup.of(1L, Lists.newArrayList(주문테이블1, 주문테이블2));
        주문상품 = OrderLineItem.of(메뉴, 1L);
        주문 = Order.from(주문테이블1);
        주문.addOrderLineItems(Collections.singletonList(주문상품));

        주문상품_요청 = OrderLineItemRequest.of(1L, 1L);
        주문_요청 = OrderRequest.of(1L, Collections.singletonList(주문상품_요청));
        주문완료_요청 = OrderRequest.from(OrderStatus.COMPLETION);

        주문테이블_응답 = OrderTableResponse.from(주문테이블1);
        주문상품_응답 = OrderLineItemResponse.from(주문상품);
        주문_응답 = OrderResponse.of(1L, 주문테이블_응답, OrderStatus.COOKING, LocalDateTime.now(), Collections.singletonList(주문상품_응답));
        주문완료_응답 = OrderResponse.of(1L, 주문테이블_응답, OrderStatus.COMPLETION, LocalDateTime.now(), Collections.singletonList(주문상품_응답));
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() throws Exception {
        given(orderService.create(any())).willReturn(주문_응답);

        final ResultActions actions = mvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(주문_요청)))
                .andDo(print());

        actions.andExpect(status().isCreated())
                .andExpect(header().stringValues("Location", "/api/orders/1"))
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("orderTableResponse").hasJsonPath())
                .andExpect(jsonPath("orderStatus").value(OrderStatus.COOKING.name()))
                .andExpect(jsonPath("orderedTime").hasJsonPath())
                .andExpect(jsonPath("orderLineItems").isArray());
    }

    @DisplayName("주문을 조회한다.")
    @Test
    void list() throws Exception {
        
        given(orderService.list()).willReturn(Collections.singletonList(주문_응답));

        final ResultActions actions = mvc.perform(get("/api/orders")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());

        actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/json;charset=UTF-8")))
                .andExpect(content().string(containsString("COOKING")));
    }

    @DisplayName("주문 상태를 완료로 변경한다.")
    @Test
    void changeOrderStatus() throws Exception {
        
        given(orderService.changeOrderStatus(anyLong(), any())).willReturn(주문완료_응답);

        
        final ResultActions actions = mvc.perform(put("/api/orders/{orderId}/order-status", 주문완료_응답.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(주문완료_요청)))
                .andDo(print());

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("orderTableResponse").hasJsonPath())
                .andExpect(jsonPath("orderedTime").hasJsonPath())
                .andExpect(jsonPath("orderLineItems").isArray())
                .andExpect(jsonPath("orderStatus").value(OrderStatus.COMPLETION.name()));
    }
}
