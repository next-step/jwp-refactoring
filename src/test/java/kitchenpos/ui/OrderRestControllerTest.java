package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.ui.api.OrderRestController;
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
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderRestController.class)
class OrderRestControllerTest {
    private static final String URI = "/api/orders";

    @Autowired
    private OrderRestController orderRestController;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private Order 주문;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        주문 = Order.of(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                Lists.list(OrderLineItem.of(1L, 1L, 1L, 1)));
    }

    @DisplayName("주문을 추가한다.")
    @Test
    void create() throws Exception {
        //given
        given(orderService.create(any())).willReturn(주문);

        //when
        ResultActions actions = mockMvc.perform(post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(주문)));

        //then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("location", URI + "/1"))
                .andExpect(content().string(containsString("1")))
                .andExpect(content().string(containsString("COOKING")));
    }

    @DisplayName("주문을 모두 조회한다.")
    @Test
    void list() throws Exception {
        //given
        given(orderService.list()).willReturn(Lists.list(주문));

        //when
        ResultActions actions = mockMvc.perform(get(URI));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].orderStatus").value(OrderStatus.COOKING.name()));
    }

    @DisplayName("특정 주문의 상태를 변경한다.")
    @Test
    void changeOrderStatus() throws Exception {
        //given
        given(orderService.changeOrderStatus(any(), any())).willReturn(주문);

        //when
        ResultActions actions = mockMvc.perform(put(URI + "/{orderId}/order-status", 주문.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(주문)));

        //then
        actions.andExpect(status().isOk());
    }

    @DisplayName("주문을 추가한다.2")
    @Test
    void create2() throws Exception {
        //given
        given(orderService.create(any())).willReturn(주문);

        //when
        ResultActions actions = mockMvc.perform(post(URI+"2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(주문)));

        //then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("location", URI+"2" + "/1"))
                .andExpect(content().string(containsString("1")))
                .andExpect(content().string(containsString("COOKING")));
    }

    @DisplayName("주문을 모두 조회한다.2")
    @Test
    void list2() throws Exception {
        //given
        given(orderService.list()).willReturn(Lists.list(주문));

        //when
        ResultActions actions = mockMvc.perform(get(URI+"2"));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].orderStatus").value(OrderStatus.COOKING.name()));
    }

    @DisplayName("특정 주문의 상태를 변경한다.2")
    @Test
    void changeOrderStatus2() throws Exception {
        //given
        given(orderService.changeOrderStatus(any(), any())).willReturn(주문);

        //when
        ResultActions actions = mockMvc.perform(put(URI+"2" + "/{orderId}/order-status", 주문.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(주문)));

        //then
        actions.andExpect(status().isOk());
    }
}
