package kitchenpos.ui;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("주문 Controller Test")
class OrderRestControllerTest extends RestControllerTest {

    public static final String ORDERS_URL = "/api/orders";

    private OrderRequest orderRequest;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        orderRequest = new OrderRequest(1L, OrderStatus.COOKING);
        orderRequest.setOrderLineItems(Arrays.asList(
                new OrderLineItemRequest(1L,1),
                new OrderLineItemRequest(2L,1))
        );
        orderRequest.setOrderTableId(1L);
    }

    @DisplayName("주문을 신청한다.")
    @Test
    void create() throws Exception {
        //given
        //when
        //then
        주문요청(orderRequest)
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(redirectedUrlPattern(ORDERS_URL+ "/*"))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.orderStatus", is(OrderStatus.COOKING.name())))
                .andExpect(jsonPath("$.orderLineItems.length()", is(2)))
                .andExpect(jsonPath("$.orderTableId", is(1)));
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() throws Exception {
        //given
        주문요청(orderRequest);
        //when
        //then
        mockMvc.perform(get(ORDERS_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0]['orderStatus']", is(OrderStatus.COOKING.name())))
                .andExpect(jsonPath("$[0]['orderLineItems'].length()", is(2)));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() throws Exception {
        //given
        ResultActions resultActions = 주문요청(orderRequest);
        String redirectedUrl = getRedirectedUrl(resultActions);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("orderStatus", OrderStatus.MEAL.name());

        //when
        //then
        mockMvc.perform(
                put(redirectedUrl + "/order-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(requestBody))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderStatus", is(OrderStatus.MEAL.name())));
    }

    private ResultActions 주문요청(OrderRequest orderRequest) throws Exception {
        return mockMvc.perform(
                post(ORDERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(orderRequest))
        )
                .andDo(print());
    }
}
