package kitchenpos.order.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.config.MockMvcTestConfig;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.ChangeOrderStatusRequest;
import kitchenpos.order.dto.CreateOrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.CreateOrderLineItemRequest;
import org.assertj.core.util.Lists;
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
import static org.junit.jupiter.api.Assertions.fail;
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

    private CreateOrderLineItemRequest 후라이드치킨하나;

    @BeforeEach
    void setUp() {
        후라이드치킨하나 = new CreateOrderLineItemRequest(1L, 1);
    }

    @DisplayName("주문 생성 요청 성공")
    @Test
    void createOrderRequestSuccess() throws Exception {

        CreateOrderLineItemRequest item = new CreateOrderLineItemRequest(1L, 1);
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(9L, Lists.newArrayList(item));

        mockMvc.perform(post(BASE_URL).content(objectMapper.writeValueAsString(createOrderRequest))
                                      .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(header().exists("Location"))
               .andReturn();
    }

    @DisplayName("주문 생성 요청 실패 - 주문 항목 포함되지 않음")
    @Test
    void createOrderRequestFail01() {
        postFail(new CreateOrderRequest());
    }

    @DisplayName("주문 생성 요청 실패 - 주문 항목에 메뉴가 중복")
    @Test
    void createOrderRequestFail02() {
        CreateOrderRequest dto = new CreateOrderRequest(1L, Stream.generate(() -> 후라이드치킨하나)
                                                                  .limit(5)
                                                                  .collect(toList()));
        postFail(dto);
    }

    @DisplayName("주문 생성 요청 실패 - order table을 찾을 수 없음")
    @Test
    void createOrderRequestFail03() {
        CreateOrderRequest dto = new CreateOrderRequest(NOT_FOUND_ID, Lists.newArrayList(후라이드치킨하나));
        postFail(dto);
    }

    @DisplayName("주문 생성 요청 실패 - empty 상태인 order table")
    @Test
    void createOrderRequestFail04() {
        long emptyOrderTableId = 1L;
        CreateOrderRequest dto = new CreateOrderRequest(emptyOrderTableId, Lists.newArrayList(후라이드치킨하나));
        postFail(dto);
    }

    @DisplayName("주문 조회 요청 성공")
    @Test
    void findOrderRequestSuccess() throws Exception {
        MvcResult result = mockMvc.perform(get(BASE_URL))
                                  .andDo(print())
                                  .andExpect(status().isOk())
                                  .andReturn();

        String response = result.getResponse().getContentAsString();
        List<OrderResponse> orders = Arrays.asList(objectMapper.readValue(response, OrderResponse[].class));
        assertThat(orders.size()).isGreaterThanOrEqualTo(2);
    }

    @DisplayName("주문 상태 변경 요청 성공")
    @Test
    void updateOrderStatusRequestSuccess() throws Exception {

        ChangeOrderStatusRequest dto = new ChangeOrderStatusRequest(OrderStatus.MEAL.name());

        MvcResult result = mockMvc.perform(put(BASE_URL + "/1/order-status")
                                               .content(objectMapper.writeValueAsString(dto))
                                               .contentType(MediaType.APPLICATION_JSON))
                                  .andDo(print())
                                  .andExpect(status().isOk())
                                  .andReturn();

        String response = result.getResponse().getContentAsString();
        OrderResponse actual = objectMapper.readValue(response, OrderResponse.class);
        assertThat(actual.getOrderStatus()).isEqualTo(dto.getOrderStatus());
    }

    @DisplayName("주문 상태 변경 요청 실패 - order id를 찾을 수 없음")
    @Test
    void updateOrderStatusRequestFail01() {
        putFail(NOT_FOUND_ID, new ChangeOrderStatusRequest("COOKING"));
    }

    @DisplayName("주문 상태 변경 요청 실패 - 이미 완료(COMPLETION 상태)된 주문")
    @Test
    void updateOrderStatusRequestFail02() {
        // V2__insert_default_data.sql 에 id = 3인 데이터 추가
        putFail(3, new ChangeOrderStatusRequest("COOKING"));
    }

    private void postFail(CreateOrderRequest dto) {
        try {
            mockMvc.perform(post(BASE_URL).content(objectMapper.writeValueAsString(dto))
                                          .contentType(MediaType.APPLICATION_JSON))
                   .andDo(print())
                   .andExpect(status().isBadRequest());
        } catch (Exception e) {
            fail();
        }
    }

    private void putFail(long orderId, ChangeOrderStatusRequest request) {
        try {
            putRequestFail(orderId, request);
        } catch (Exception e) {
            fail();
        }
    }

    private void putRequestFail(long orderId, ChangeOrderStatusRequest request) throws Exception {
        mockMvc.perform(put(BASE_URL + "/" + orderId + "/order-status")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest());
    }
}
