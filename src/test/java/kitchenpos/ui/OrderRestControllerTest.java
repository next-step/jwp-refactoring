package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderRestController.class)
class OrderRestControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    OrderRestController orderRestController;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    OrderService orderService;
    Order 주문;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        주문 = new Order();
        주문.setId(1L);
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void create() throws Exception {
        //given
        String requestBody = objectMapper.writeValueAsString(주문);

        when(orderService.create(any())).thenReturn(주문);

        //when && then
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("전체 주문을 조회한다.")
    void list() throws Exception {
        //given
        when(orderService.list()).thenReturn(Arrays.asList(주문));

        //when && then
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":1")));
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() throws Exception {
        //given
        주문.setOrderStatus(OrderStatus.MEAL.name());
        String requestBody = objectMapper.writeValueAsString(주문);

        Order 주문_응답 = new Order();
        주문_응답.setOrderStatus(OrderStatus.MEAL.name());

        when(orderService.changeOrderStatus(any(), any())).thenReturn(주문_응답);

        //when && then
        mockMvc.perform(put("/api/orders/{orderId}/order-status", 주문.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("MEAL")));
    }
}