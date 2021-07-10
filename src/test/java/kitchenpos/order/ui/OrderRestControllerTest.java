package kitchenpos.order.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.ui.OrderRestController;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

//import static kitchenpos.order.application.OrderServiceTest.주문_생성;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("주문 관련 기능 테스트")
@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {
    private static final String ORDER_URI = "/api/orders";
    private static final String ORDER_STATUS_CHANGE_URI = "/{orderId}/order-status";

    private Product product;
    private OrderResponse order;
    private MenuProduct menuProduct;
    private Menu menu;
    private OrderLineItem orderLIneItem;
    private Order order1;
    private OrderLineItemRequest orderLineItemRequest;
    private OrderRequest orderRequest;
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRestController orderRestController;

    @BeforeEach
    void setUp() {
        setUpMockMvc();
        메뉴_생성();
        주문_생성();
        주문_요청값_생성();
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() throws Exception {
        given(orderService.create(any())).willReturn(order);

        final ResultActions actions = 주문_생성_요청(orderRequest);

        주문_생성됨(actions);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() throws Exception {
        given(orderService.list()).willReturn(Arrays.asList(order));

        final ResultActions actions = 주문_목록_조회_요청();

        주문_목록_조회됨(actions);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() throws Exception {
        given(orderService.changeOrderStatus(order.getId(), orderRequest)).willReturn(order);

        final ResultActions actions = 주문_상태_변경_요청(orderRequest);

        주문_상태_변경됨(actions);
    }

    private void 메뉴_생성() {
        product = new Product(1L, new Name("뿌링클순살"), new Price(new BigDecimal(18000)));
        menuProduct = new MenuProduct(1L, product, 1L);
        menu = new Menu(1L, new Name("뿌링클치즈볼"), new Price(new BigDecimal(18000)), new MenuGroup(1L, "뿌링클 시리즈"), Arrays.asList(menuProduct));

    }

    private void 주문_생성() {
        orderLIneItem = new OrderLineItem(1L, menu, 1L);
        order1 = new Order(1L, mock(OrderTable.class), OrderStatus.COOKING, Arrays.asList(orderLIneItem));
        order = new OrderResponse(order1);
    }

    private void 주문_요청값_생성() {
        orderLineItemRequest = new OrderLineItemRequest(1L, 1L, 1L, 3);
        orderRequest = new OrderRequest(1L, OrderStatus.COOKING, Arrays.asList(orderLineItemRequest));
    }

    private void setUpMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();
    }

    private ResultActions 주문_생성_요청(OrderRequest order) throws Exception {
        return  mockMvc.perform(post(ORDER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)));
    }


    private void 주문_생성됨(ResultActions actions) throws Exception{
        actions.andExpect(status().isCreated())
                .andExpect(header().string("location", "/api/orders/1"))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    private ResultActions 주문_목록_조회_요청() throws Exception{
        return mockMvc.perform(get(ORDER_URI));
    }

    private void 주문_목록_조회됨(ResultActions actions) throws Exception{
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].orderStatus").value(OrderStatus.COOKING.name()));
    }

    private ResultActions 주문_상태_변경_요청(OrderRequest orderRequest) throws Exception{
        return mockMvc.perform(put(ORDER_URI + ORDER_STATUS_CHANGE_URI , 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)));
    }

    private void 주문_상태_변경됨(ResultActions actions) throws Exception{
        actions.andExpect(status().isOk());
    }
}
