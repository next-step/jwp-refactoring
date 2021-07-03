package kitchenpos.order.ui;

import static kitchenpos.util.TestDataSet.주문_1번;
import static kitchenpos.util.TestDataSet.주문_2번;
import static kitchenpos.util.TestDataSet.테이블_2번;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.ui.OrderRestController;
import kitchenpos.product.domain.OrderLineItem;

@WebMvcTest(controllers = OrderRestController.class)
@ExtendWith(MockitoExtension.class)
public class OrderRestControllerTest {

    private static final String BASE_URL = "/api/orders";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    @DisplayName("손님이 메뉴를 선택하면 신규 주문을 할 수 있다.")
    void create() throws Exception {
        // given
        String content = objectMapper.writeValueAsString(주문_1번);
        given(orderService.create(any())).willReturn(주문_1번);

        // when
        mockMvc.perform(
            post(BASE_URL)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(주문_1번.getId()))
            .andExpect(jsonPath("$.orderLineItems[0].menuId").value(1L))
            .andExpect(jsonPath("$.orderLineItems[1].menuId").value(2L));
    }

    @Test
    @DisplayName("주문 전체 리시트를 출력할 수 있다.")
    void list() throws Exception {
        // given
        given(orderService.list())
            .willReturn(Arrays.asList(주문_1번, 주문_2번));

        // when
        mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(주문_1번.getId()))
            .andExpect(jsonPath("$[1].id").value(주문_2번.getId()));
    }

    @Test
    @DisplayName("현재 주문의 상태를 업데이트 할 수 있다.")
    void changeOrderStatus() throws Exception {
        // given
        Order 상태_업데이트된_주문 = new Order(1L, 테이블_2번.getId(),
            Arrays.asList(new OrderLineItem(1L, 10), new OrderLineItem(2L, 10)));
        상태_업데이트된_주문.setOrderStatus("MEAL");

        String content = objectMapper.writeValueAsString(상태_업데이트된_주문);

        given(orderService.changeOrderStatus(any(), any()))
            .willReturn(상태_업데이트된_주문);

        // when
        mockMvc.perform(
            put(BASE_URL + "/{orderId}/order-status", 주문_1번.getId())
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.orderStatus").value(상태_업데이트된_주문.getOrderStatus()));
    }

}
