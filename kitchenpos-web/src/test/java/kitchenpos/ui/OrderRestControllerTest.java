package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.*;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.*;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.AfterEach;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderRestControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    OrderLineItemRepository orderLineItemRepository;

    OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = new MenuGroup("패스트푸드");
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
        Menu menu = new Menu("햄버거", BigDecimal.valueOf(5000), savedMenuGroup);
        Menu savedMenu = menuRepository.save(menu);

        orderLineItem = new OrderLineItem(savedMenu.getId(), new Quantity(10));

        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @AfterEach
    void afterTest() {
        orderRepository.deleteAll();
        orderTableRepository.deleteAll();
    }

    @DisplayName("주문등록 api 테스트")
    @Test
    public void create() throws Exception {
        OrderTable orderTable = new OrderTable(4, false);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        Order order = new Order(savedOrderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(), Arrays.asList(orderLineItem));

        String requestBody = objectMapper.writeValueAsString(order);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isCreated())
        ;

    }

    @DisplayName("주문 목록 Api 테스트")
    @Test
    void list() throws Exception {
        OrderTable orderTable = new OrderTable(4, false);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        LocalDateTime orderedTime = LocalDateTime.now();

        Order order = new Order(savedOrderTable.getId(), OrderStatus.COOKING, orderedTime, Arrays.asList(orderLineItem));
        order.reception();
        orderRepository.save(order);

        mockMvc.perform(get("/api/orders")
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @DisplayName("주문 상태 변경 Api 테스트")
    @Test
    void changeOrderStatus() throws Exception {
        LocalDateTime orderedTime = LocalDateTime.now();

        OrderTable orderTable = new OrderTable(4, false);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        Order order = new Order(savedOrderTable.getId(), OrderStatus.COOKING, orderedTime, Arrays.asList(orderLineItem));
        order.reception();
        Order savedOrder = orderRepository.save(order);

        String requestBody = objectMapper.writeValueAsString(order);

        mockMvc.perform(put("/api/orders/" + savedOrder.getId() + "/order-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }
}
