package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.config.MockMvcTestConfig;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.ChangeOrderStatusDto;
import kitchenpos.order.dto.CreateOrderDto;
import kitchenpos.order.dto.OrderDto;
import kitchenpos.order.dto.OrderLineItemDto;
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

    private OrderLineItemDto 후라이드치킨하나;

    @BeforeEach
    void setUp() {
        후라이드치킨하나 = new OrderLineItemDto(1L, 1);
    }

    @DisplayName("주문 생성 요청 성공")
    @Test
    void createOrderRequestSuccess() throws Exception {

        OrderLineItemDto item = new OrderLineItemDto(1L, 1);
        CreateOrderDto createOrderDto = new CreateOrderDto(9L, Lists.newArrayList(item));

        mockMvc.perform(post(BASE_URL).content(objectMapper.writeValueAsString(createOrderDto))
                                      .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(header().exists("Location"))
               .andReturn();
    }

    @DisplayName("주문 생성 요청 실패 - 주문 항목 포함되지 않음")
    @Test
    void createOrderRequestFail01() {
        postFail(new CreateOrderDto());
    }

    @DisplayName("주문 생성 요청 실패 - 주문 항목에 메뉴가 중복")
    @Test
    void createOrderRequestFail02() {
        CreateOrderDto dto = new CreateOrderDto(1L, Stream.generate(() -> 후라이드치킨하나)
                                                          .limit(5)
                                                          .collect(toList()));
        postFail(dto);
    }

    @DisplayName("주문 생성 요청 실패 - order table을 찾을 수 없음")
    @Test
    void createOrderRequestFail03() {
        CreateOrderDto dto = new CreateOrderDto(NOT_FOUND_ID, Lists.newArrayList(후라이드치킨하나));
        postFail(dto);
    }

    @DisplayName("주문 생성 요청 실패 - empty 상태인 order table")
    @Test
    void createOrderRequestFail04() {
        long emptyOrderTableId = 1L;
        CreateOrderDto dto = new CreateOrderDto(emptyOrderTableId, Lists.newArrayList(후라이드치킨하나));
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
        List<OrderDto> orders = Arrays.asList(objectMapper.readValue(response, OrderDto[].class));
        assertThat(orders.size()).isGreaterThanOrEqualTo(2);
    }

    @DisplayName("주문 상태 변경 요청 성공")
    @Test
    void updateOrderStatusRequestSuccess() throws Exception {

        ChangeOrderStatusDto dto = new ChangeOrderStatusDto(OrderStatus.MEAL.name());

        MvcResult result = mockMvc.perform(put(BASE_URL + "/1/order-status")
                                               .content(objectMapper.writeValueAsString(dto))
                                               .contentType(MediaType.APPLICATION_JSON))
                                  .andDo(print())
                                  .andExpect(status().isOk())
                                  .andReturn();

        String response = result.getResponse().getContentAsString();
        OrderDto actual = objectMapper.readValue(response, OrderDto.class);
        assertThat(actual.getOrderStatus()).isEqualTo(dto.getOrderStatus());
    }

    @DisplayName("주문 상태 변경 요청 실패 - order id를 찾을 수 없음")
    @Test
    void updateOrderStatusRequestFail01() {
        putFail(NOT_FOUND_ID, new ChangeOrderStatusDto("COOKING"));
    }

    @DisplayName("주문 상태 변경 요청 실패 - 이미 완료(COMPLETION 상태)된 주문")
    @Test
    void updateOrderStatusRequestFail02() {
        // V2__insert_default_data.sql 에 id = 3인 데이터 추가
        putFail(3, new ChangeOrderStatusDto("COOKING"));
    }

    private void postFail(CreateOrderDto dto) {
        try {
            mockMvc.perform(post(BASE_URL).content(objectMapper.writeValueAsString(dto))
                                          .contentType(MediaType.APPLICATION_JSON))
                   .andDo(print())
                   .andExpect(status().isBadRequest());
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }

    private void putFail(long orderId, ChangeOrderStatusDto changeOrderStatusDto) {
        try {
            putRequestFail(orderId, changeOrderStatusDto);
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }

    private void putRequestFail(long orderId, ChangeOrderStatusDto changeOrderStatusDto) throws Exception {
        mockMvc.perform(put(BASE_URL + "/" + orderId + "/order-status")
                            .content(objectMapper.writeValueAsString(changeOrderStatusDto))
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest());
    }
}
