package kitchenpos.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderRestControllerTest {
    MockMvc mockMvc;
    @Autowired
    OrderRestController orderRestController;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    OrderService orderService;
    @Autowired
    WebApplicationContext webApplicationContext;

    Order 주문;
    OrderTable 오더테이블;
    OrderLineItem 존재하지않는_메뉴를_가진_주문서;
    long 비어있는_테스트데이터_테이블_아이디 = 3L;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(1L);
        orderLineItem.setMenuId(2L);
        orderLineItem.setQuantity(1L);

        주문 = new Order();
        주문.setOrderTableId(99L);
        주문.setOrderLineItems(Arrays.asList(orderLineItem));

        오더테이블 = new OrderTable();
        오더테이블.setNumberOfGuests(3);
        오더테이블.setEmpty(false);

        존재하지않는_메뉴를_가진_주문서 = new OrderLineItem();
        존재하지않는_메뉴를_가진_주문서.setOrderId(1L);
        존재하지않는_메뉴를_가진_주문서.setMenuId(99L);
        존재하지않는_메뉴를_가진_주문서.setQuantity(1L);
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void create() throws Exception {
        //given
        String requestBody = objectMapper.writeValueAsString(주문);

        //when && then
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("주문항목이 비어있을 경우 주문 생성을 실패한다.")
    void create_with_exception_when_order_line_items_is_empty() throws JsonProcessingException {
        //given
        주문.setOrderLineItems(Arrays.asList());

        //when && then
        주문_생성_요청_실패();
    }

    @Test
    @DisplayName("저장된 메뉴에 없는 메뉴일 경우 주문 생성을 실패한다.")
    void create_with_exception_when_menu_not_in_saved_menus() throws JsonProcessingException {
        //given
        주문.setOrderLineItems(Arrays.asList(존재하지않는_메뉴를_가진_주문서));

        //when && then
        주문_생성_요청_실패();
    }

    @Test
    @DisplayName("빈 테이블에서 주문할 경우 주문 생성을 실패한다.")
    void create_with_exception_when_table_is_null() throws JsonProcessingException {
        //given
        주문.setOrderTableId(비어있는_테스트데이터_테이블_아이디);

        //when && then
        주문_생성_요청_실패();
    }

    @Test
    @DisplayName("전체 주문을 조회한다.")
    void list() throws Exception {
        //when && then
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":1")));
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() throws Exception {
        //given
        주문.setId(1L);
        주문.setOrderStatus(OrderStatus.MEAL.name());
        String requestBody = objectMapper.writeValueAsString(주문);

        //when && then
        mockMvc.perform(put("/api/orders/{orderId}/order-status", 주문.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("MEAL")));
    }

    private void 주문_생성_요청_실패() throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(주문);

        try {
            mockMvc.perform(post("/api/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
        }
    }
}