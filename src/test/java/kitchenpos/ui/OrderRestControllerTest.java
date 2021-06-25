package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.config.MockMvcTestConfig;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@MockMvcTestConfig
@SuppressWarnings("NonAsciiCharacters")
class OrderRestControllerTest {

    private static final String BASE_URL = "/api/orders";
    private static final Long NOT_FOUND_ID = Long.MAX_VALUE;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Order successOrder;

    private OrderLineItem 후라이드치킨하나;

    @BeforeEach
    void setUp() {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        OrderLineItem item = new OrderLineItem();
        item.setMenuId(1L);
        item.setQuantity(1);
        orderLineItems.add(item);

        successOrder = new Order();
        successOrder.setOrderLineItems(orderLineItems);
        successOrder.setOrderTableId(9L);

        후라이드치킨하나 = new OrderLineItem();
        후라이드치킨하나.setMenuId(1L);
        후라이드치킨하나.setQuantity(1);
    }

    @DisplayName("주문 생성 요청 성공")
    @Test
    void createOrderRequestSuccess() throws Exception {
        mockMvc.perform(post(BASE_URL).content(objectMapper.writeValueAsString(successOrder))
                                      .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(header().exists("Location"));
    }

    @DisplayName("주문 생성 요청 실패 - 주문 항목 포함되지 않음")
    @Test
    void createOrderRequestFail01() {
        postFail(new Order());
    }

    @DisplayName("주문 생성 요청 실패 - 주문 항목에 메뉴가 중복")
    @Test
    void createOrderRequestFail02() {

        Order order = new Order();
        order.setOrderLineItems(Stream.generate(() -> 후라이드치킨하나)
                                      .limit(5)
                                      .collect(toList()));

        postFail(order);
    }

    @DisplayName("주문 생성 요청 실패 - order table을 찾을 수 없음")
    @Test
    void createOrderRequestFail03() {

        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(후라이드치킨하나));
        order.setOrderTableId(NOT_FOUND_ID);

        postFail(order);
    }

    @DisplayName("주문 생성 요청 실패 - empty 상태인 order table")
    @Test
    void createOrderRequestFail04() {

        long emptyOrderTableId = 1L;

        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(후라이드치킨하나));
        order.setOrderTableId(emptyOrderTableId);

        postFail(order);
    }

    @DisplayName("주문 조회 요청 성공")
    @Test
    void findOrderRequestSuccess() throws Exception {
        MvcResult result = mockMvc.perform(get(BASE_URL))
                                  .andDo(print())
                                  .andExpect(status().isOk())
                                  .andReturn();

        String response = result.getResponse().getContentAsString();
        List<Order> orders = Arrays.asList(objectMapper.readValue(response, Order[].class));
        assertThat(orders.size()).isGreaterThanOrEqualTo(2);
    }

    @DisplayName("주문 상태 변경 요청 성공")
    @Test
    void updateOrderStatusRequestSuccess() throws Exception {

        Order order = new Order();
        order.setOrderStatus("MEAL");

        MvcResult result = mockMvc.perform(put(BASE_URL + "/1/order-status")
                                               .content(objectMapper.writeValueAsString(order))
                                               .contentType(MediaType.APPLICATION_JSON))
                                  .andDo(print())
                                  .andExpect(status().isOk())
                                  .andReturn();

        String response = result.getResponse().getContentAsString();
        Order actual = objectMapper.readValue(response, Order.class);
        assertThat(actual.getOrderStatus()).isEqualTo(order.getOrderStatus());
    }

    @DisplayName("주문 상태 변경 요청 실패 - order id를 찾을 수 없음")
    @Test
    void updateOrderStatusRequestFail01() {
        putFail(NOT_FOUND_ID, new Order());
    }

    @DisplayName("주문 상태 변경 요청 실패 - 이미 완료(COMPLETION 상태)된 주문")
    @Test
    void updateOrderStatusRequestFail02() {
        // V2__insert_default_data.sql 에 id = 3인 데이터 추가
        putFail(3, new Order());
    }

    private void postFail(Order order) {
        try {
            mockMvc.perform(post(BASE_URL).content(objectMapper.writeValueAsString(order))
                                          .contentType(MediaType.APPLICATION_JSON))
                   .andDo(print())
                   .andExpect(status().isBadRequest());
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }

    private void putFail(long orderId, Order order) {
        try {
            putRequestFail(orderId, order);
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }

    private void putRequestFail(long orderId, Order order) throws Exception {
        mockMvc.perform(put(BASE_URL + "/" + orderId + "/order-status")
                            .content(objectMapper.writeValueAsString(order))
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest());
    }
}
