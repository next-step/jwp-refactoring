package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class OrderRestControllerTest {

    private Order order;
    private Order order2;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);
        order.setOrderTableId(1L);

        order2 = new Order();
        order2.setId(2L);
        order2.setOrderTableId(1L);
    }

    @DisplayName("주문을 등록한다")
    @Test
    void create() throws Exception {
        // given
        given(orderService.create(any())).willReturn(order);

        // when then
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("/api/orders/" + order.getId()));
    }

    @DisplayName("전체 주문 목록을 조회한다")
    @Test
    void list() throws Exception {
        // given
        given(orderService.list()).willReturn(Arrays.asList(order, order2));

        // when then
        mockMvc.perform(get("/api/orders"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("주문의 상태를 변경한다")
    @Test
    void changeOrderStatus() throws Exception {
        // given
        Order orderParam = new Order();
        given(orderService.changeOrderStatus(any(), any())).willReturn(this.order);

        // when then
        mockMvc.perform(put("/api/orders/" + order.getId() + "/order-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderParam)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}