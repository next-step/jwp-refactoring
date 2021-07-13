package kitchenpos.order.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import kitchenpos.order.domain.entity.OrderTable;
import kitchenpos.order.domain.value.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootTest
@AutoConfigureMockMvc
class OrderRestControllerTest {

    MockMvc mockMvc;
    @Autowired
    OrderRestController orderRestController;
    @Autowired
    ObjectMapper objectMapper;

    OrderRequest 주문_리퀘스트;
    OrderLineItemRequest 오더라인아이템_리퀘스트;
    OrderTable 오더테이블;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderRestController)
            .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
            .alwaysDo(print())
            .build();

        오더라인아이템_리퀘스트 = new OrderLineItemRequest(1L, 1L, 1L);
        주문_리퀘스트 = new OrderRequest(1L, 7L, Arrays.asList(오더라인아이템_리퀘스트));
        오더테이블 = new OrderTable();
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void create() throws Exception {
        //given
        String requestBody = objectMapper.writeValueAsString(주문_리퀘스트);

        //when && then
        mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("저장된 메뉴에 없는 메뉴일 경우 주문 생성을 실패한다.")
    void create_with_exception_when_menu_not_in_saved_menus() throws JsonProcessingException {
        //given
        오더라인아이템_리퀘스트 = new OrderLineItemRequest(999L, 999L, 1L);
        주문_리퀘스트 = new OrderRequest(1L, 1L, Arrays.asList(오더라인아이템_리퀘스트));

        //when && then
        주문_생성_요청_실패();
    }

    @Test
    @DisplayName("빈 테이블에서 주문할 경우 주문 생성을 실패한다.")
    void create_with_exception_when_table_is_null() throws JsonProcessingException {
        //given
        주문_리퀘스트 = new OrderRequest(1L, 6L, Arrays.asList(오더라인아이템_리퀘스트));

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
        주문_리퀘스트 = new OrderRequest(1L, 7L, OrderStatus.MEAL.name(), Arrays.asList(오더라인아이템_리퀘스트));

        String requestBody = objectMapper.writeValueAsString(주문_리퀘스트);

        //when && then
        mockMvc.perform(put("/api/orders/{orderId}/order-status", 주문_리퀘스트.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("MEAL")));
    }

    private void 주문_생성_요청_실패() throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(주문_리퀘스트);

        try {
            mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
        }
    }
}