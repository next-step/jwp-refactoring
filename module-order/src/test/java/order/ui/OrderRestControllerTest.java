package order.ui;

import static order.fixture.OrderFixture.주문_요청_데이터_생성;
import static order.fixture.OrderFixture.주문_응답_데이터_생성;
import static order.fixture.OrderLineItemFixture.주문항목_응답_데이터_생성;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import common.domain.OrderStatus;
import common.ui.BaseRestControllerTest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import order.application.OrderService;
import order.dto.OrderLineItemRequestDto;
import order.dto.OrderLineItemResponseDto;
import order.dto.OrderRequestDto;
import order.dto.OrderResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class OrderRestControllerTest extends BaseRestControllerTest {

    @Mock
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new OrderRestController(orderService)).build();
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create() throws Exception {
        //given
        List<OrderLineItemRequestDto> orderLineItemRequests = Arrays.asList(new OrderLineItemRequestDto(1L, 1));
        OrderRequestDto request = 주문_요청_데이터_생성(orderLineItemRequests);
        String requestBody = objectMapper.writeValueAsString(request);

        List<OrderLineItemResponseDto> orderLineItems = Arrays.asList(주문항목_응답_데이터_생성(1L, 1L, 1));
        OrderResponseDto response = 주문_응답_데이터_생성(1L, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        given(orderService.create(any())).willReturn(response);

        //when //then
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.orderTableId").value(1L))
                .andExpect(jsonPath("$.orderStatus").value(OrderStatus.COOKING.name()))
                .andExpect(jsonPath("$.orderedTime").isNotEmpty())
                .andExpect(jsonPath("$.orderLineItems").isNotEmpty());
    }

    @DisplayName("주문과 주문항목을 전체 조회한다.")
    @Test
    void list() throws Exception {
        //given
        List<OrderLineItemResponseDto> orderLineItems = Arrays.asList(주문항목_응답_데이터_생성(1L, 1L, 1));
        OrderResponseDto response = 주문_응답_데이터_생성(1L, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        given(orderService.list()).willReturn(Arrays.asList(response));

        //when //then
        mockMvc.perform(get("/api/orders"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @DisplayName("주문상태를 변경한다.")
    @Test
    void changeOrderStatus() throws Exception {
        //given
        Object orderId = 1L;
        String requestBody = objectMapper.writeValueAsString(OrderStatus.COMPLETION);

        List<OrderLineItemResponseDto> orderLineItems = Arrays.asList(주문항목_응답_데이터_생성(1L, 1L, 1));
        OrderResponseDto response = 주문_응답_데이터_생성(1L, OrderStatus.COMPLETION, LocalDateTime.now(), orderLineItems);
        given(orderService.changeOrderStatus(any(), any())).willReturn(response);

        //when //then
        mockMvc.perform(put("/api/orders/{orderId}/order-status", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.orderStatus").value(OrderStatus.COMPLETION.name()));
    }
}