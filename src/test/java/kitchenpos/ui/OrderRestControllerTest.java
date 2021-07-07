package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;


@DisplayName("주문 관련 테스트")
@SpringBootTest
class OrderRestControllerTest {
    public static final String ORDER_URI = "/api/orders";

    private Order order;

    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp(@Autowired OrderRestController orderRestController) {
        // MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(orderRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());
    }

    public String toJsonString(Order order) throws JsonProcessingException {
        return objectMapper.writeValueAsString(order);
    }

    @Test
    @DisplayName("주문을 등록할 수 있다.")
    public void create() throws Exception {
        // given
        given(orderService.create(any())).willReturn(order);

        // when
        final ResultActions actions = mockMvc.perform(post(ORDER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(order)));

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/api/orders/1"))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @DisplayName("주문의 목록을 조회할 수 있다.")
    public void list() throws Exception {
        // given
        given(orderService.list()).willReturn(Collections.singletonList(order));

        // when
        final ResultActions actions = mockMvc.perform(get(ORDER_URI));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].orderStatus").value(OrderStatus.COOKING.name()));
    }

    @Test
    @DisplayName("주문 상태를 변경할 수 있다.")
    public void changeOrderStatus() throws Exception {
        // given
        given(orderService.changeOrderStatus(order.getId(), order)).willReturn(order);

        // when
        final ResultActions actions = mockMvc.perform(put(ORDER_URI + "/{orderId}/order-status" , order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(order)));

        // then
        actions.andExpect(status().isOk());
    }
}
