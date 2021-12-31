package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.OrderService;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.menu.MenuProductResponse;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.menugroup.MenuGroupResponse;
import kitchenpos.dto.order.OrderLineItemResponse;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.ordertable.OrderTableResponse;
import kitchenpos.dto.product.ProductResponse;
import kitchenpos.fixture.*;
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
import java.util.List;

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

    @BeforeEach
    void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() throws Exception {
        final OrderRequest 주문_요청 = TestOrderFactory.주문_생성_요청(1L);
        final ProductResponse 상품_응답 = TestProductFactory.상품_응답(1L, "상품", 1000);
        final List<MenuProductResponse> 메뉴상품목록_응답 = TestMenuFactory.메뉴상품목록_응답(상품_응답.getId(), 2);
        final MenuGroupResponse 메뉴그룹_응답 = TestMenuGroupFactory.메뉴그룹_응답(1L, "메뉴그룹");
        final MenuResponse 메뉴_응답 = TestMenuFactory.메뉴_응답(1L, "메뉴", 50000, 메뉴그룹_응답, 메뉴상품목록_응답);

        final List<OrderLineItemResponse> orderLineItems = TestOrderTableFactory.주문상품아이템_응답(1L, 메뉴_응답, 1);
        final OrderTableResponse 주문테이블_응답 = TestOrderTableFactory.주문테이블_응답(1L, 10, false);
        final OrderResponse 주문_응답 = OrderResponse.of(1L, 주문테이블_응답, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);

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
        final ProductResponse 상품_응답 = TestProductFactory.상품_응답(1L, "상품", 1000);
        final List<MenuProductResponse> 메뉴상품목록_응답 = TestMenuFactory.메뉴상품목록_응답(상품_응답.getId()
                , 2);
        final MenuGroupResponse 메뉴그룹_응답 = TestMenuGroupFactory.메뉴그룹_응답(1L, "메뉴그룹");
        final MenuResponse 메뉴_응답 = TestMenuFactory.메뉴_응답(1L, "메뉴", 50000, 메뉴그룹_응답, 메뉴상품목록_응답);

        final List<OrderLineItemResponse> orderLineItems = TestOrderTableFactory.주문상품아이템_응답(1L, 메뉴_응답, 1);
        final OrderTableResponse 주문테이블_응답 = TestOrderTableFactory.주문테이블_응답(1L, 10, false);
        final OrderResponse 주문_응답 = OrderResponse.of(1L, 주문테이블_응답, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        
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
        final OrderRequest 주문완료_요청 = TestOrderFactory.주문_완료_요청();
        final ProductResponse 상품_응답 = TestProductFactory.상품_응답(1L, "상품", 1000);
        final List<MenuProductResponse> 메뉴상품목록_응답 = TestMenuFactory.메뉴상품목록_응답(상품_응답.getId(), 2);
        final MenuGroupResponse 메뉴그룹_응답 = TestMenuGroupFactory.메뉴그룹_응답(1L, "메뉴그룹");
        final MenuResponse 메뉴_응답 = TestMenuFactory.메뉴_응답(1L, "메뉴", 50000, 메뉴그룹_응답, 메뉴상품목록_응답);

        final List<OrderLineItemResponse> orderLineItems = TestOrderTableFactory.주문상품아이템_응답(1L, 메뉴_응답, 1);
        final OrderTableResponse 주문테이블_응답 = TestOrderTableFactory.주문테이블_응답(1L, 10, false);
        final OrderResponse 주문완료_응답 = OrderResponse.of(1L, 주문테이블_응답, OrderStatus.COMPLETION, LocalDateTime.now(), orderLineItems);
        
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
