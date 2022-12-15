package kitchenpos.ui;

import static kitchenpos.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.IntegrationTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusRequest;

class OrderRestControllerTest extends IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    private OrderTable orderTable;
    private Menu menu;
    private List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.orderTable = orderTableRepository.save(new OrderTable(0, true));
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("test"));
        this.menu = menuRepository.save(new Menu("test", BigDecimal.valueOf(100L), menuGroup.getId(), null));
        orderLineItemRequests.add(new OrderLineItemRequest(this.menu.getId(), 1L));
    }

    @DisplayName("주문을 등록한다")
    @Test
    void test1() throws Exception {
        Long id = 주문등록();
        assertThat(orderRepository.findById(id)).isNotEmpty();
    }

    @DisplayName("전체 주문을 조회한다")
    @Test
    void test2() throws Exception {
        주문등록();

        mockMvc.perform(get("/api/orders"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$..id").exists())
            .andExpect(jsonPath("$..orderTableId").exists())
            .andExpect(jsonPath("$..orderStatus").exists())
            .andExpect(jsonPath("$..orderedTime").exists())
            .andExpect(jsonPath("$..orderLineItems").exists());
    }

    @DisplayName("주문 상태를 변경한다")
    @Test
    void test3() throws Exception {
        Long id = 주문등록();
        OrderStatus orderStatus = MEAL;

        mockMvc.perform(put("/api/orders/" + id + "/order-status")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new OrderStatusRequest(orderStatus))))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.orderTableId").exists())
            .andExpect(jsonPath("$.orderStatus").value(orderStatus.name()))
            .andExpect(jsonPath("$.orderedTime").exists());

        assertThat(orderRepository.findById(id)).isNotEmpty();
    }

    private Long 주문등록() throws Exception {
        OrderRequest request = new OrderRequest(this.orderTable.getId(), orderLineItemRequests);
        MvcResult result = mockMvc.perform(post("/api/orders")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.orderTableId").value(orderTable.getId()))
            .andExpect(jsonPath("$.orderStatus").value(COOKING.name()))
            .andExpect(jsonPath("$.orderedTime").exists())
            .andExpect(jsonPath("$.orderLineItems.length()").value(orderLineItemRequests.size()))
            .andReturn();

        return getId(result);
    }

    private Long getId(MvcResult result) throws
        com.fasterxml.jackson.core.JsonProcessingException,
        UnsupportedEncodingException {
        String response = result.getResponse().getContentAsString();
        return objectMapper.readValue(response, OrderResponse.class).getId();
    }

}
