package kitchenpos.api.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.order.OrderService;
import kitchenpos.common.BaseTest;
import kitchenpos.domain.menu.*;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.dto.OrderLineItemRequest;
import kitchenpos.domain.order.dto.OrderRequest;
import kitchenpos.domain.order.dto.OrderResponse;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("주문 컨트롤러 테스트")
class OrderRestControllerTest extends BaseTest {
    public static final String DEFAULT_ORDERS_URI = "/api/orders/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    private OrderRequest orderRequest;
    private List<OrderLineItemRequest> orderLineItemRequestList;
    private OrderLineItemRequest orderLineItemRequest;

    @BeforeEach
    void setUp() {
        final Product 후라이드 = productRepository.save(Product.of("후라이드", Price.of(new BigDecimal(16_000L))));
        final Product 양념치킨 = productRepository.save(Product.of("양념치킨", Price.of(new BigDecimal(16_000L))));
        final Product 반반치킨 = productRepository.save(Product.of("반반치킨", Price.of(new BigDecimal(16_000L))));
        final Product 통구이 = productRepository.save(Product.of("통구이", Price.of(new BigDecimal(16_000L))));
        final Product 간장치킨 = productRepository.save(Product.of("간장치킨", Price.of(new BigDecimal(16_000L))));
        final Product 순살치킨 = productRepository.save(Product.of("순살치킨", Price.of(new BigDecimal(16_000L))));

        final MenuGroup newMenuGroup = new MenuGroup();
        newMenuGroup.setName("두마리메뉴");
        menuGroupRepository.save(newMenuGroup);
        newMenuGroup.setName("한마리메뉴");
        final MenuGroup secondMenuGroup = menuGroupRepository.save(newMenuGroup);
        newMenuGroup.setName("순살파닭두마리메뉴");
        menuGroupRepository.save(newMenuGroup);
        newMenuGroup.setName("신메뉴");
        menuGroupRepository.save(newMenuGroup);

        final Menu 후라이드치킨메뉴 = menuRepository.save(
            Menu.of("후라이드치킨",
                Price.of(new BigDecimal(16_000L)),
                secondMenuGroup,
                MenuProducts.of(Arrays.asList(MenuProduct.of(후라이드, 1L)))));
        final Menu 양념치킨메뉴 = menuRepository.save(
            Menu.of("양념치킨",
                Price.of(new BigDecimal(16_000L)),
                secondMenuGroup,
                MenuProducts.of(Arrays.asList(MenuProduct.of(양념치킨, 1L)))));
        final Menu 반반치킨메뉴 = menuRepository.save(
            Menu.of("반반치킨",
                Price.of(new BigDecimal(16_000L)),
                secondMenuGroup,
                MenuProducts.of(Arrays.asList(MenuProduct.of(반반치킨, 1L)))));
        final Menu 통구이메뉴 = menuRepository.save(
            Menu.of("통구이",
                Price.of(new BigDecimal(16_000L)),
                secondMenuGroup,
                MenuProducts.of(Arrays.asList(MenuProduct.of(통구이, 1L)))));
        final Menu 간장치킨메뉴 = menuRepository.save(
            Menu.of("간장치킨",
                Price.of(new BigDecimal(17_000L)),
                secondMenuGroup,
                MenuProducts.of(Arrays.asList(MenuProduct.of(간장치킨, 1L)))));
        final Menu 순살치킨메뉴 = menuRepository.save(
            Menu.of("순살치킨",
                Price.of(new BigDecimal(17_000L)),
                secondMenuGroup,
                MenuProducts.of(Arrays.asList(MenuProduct.of(순살치킨, 1L)))));

        menuProductRepository.save(후라이드치킨메뉴.getMenuProducts().get(0));
        menuProductRepository.save(양념치킨메뉴.getMenuProducts().get(0));
        menuProductRepository.save(반반치킨메뉴.getMenuProducts().get(0));
        menuProductRepository.save(통구이메뉴.getMenuProducts().get(0));
        menuProductRepository.save(간장치킨메뉴.getMenuProducts().get(0));
        menuProductRepository.save(순살치킨메뉴.getMenuProducts().get(0));

        orderTableRepository.save(OrderTable.of(null, 0, true));
        orderTableRepository.save(OrderTable.of(null, 0, true));
        orderTableRepository.save(OrderTable.of(null, 0, true));
        orderTableRepository.save(OrderTable.of(null, 0, true));
        orderTableRepository.save(OrderTable.of(null, 0, true));
        orderTableRepository.save(OrderTable.of(null, 0, true));
        orderTableRepository.save(OrderTable.of(null, 0, true));
        orderTableRepository.save(OrderTable.of(null, 0, true));

        orderLineItemRequestList = new ArrayList<>();

        orderLineItemRequest = new OrderLineItemRequest();
        orderLineItemRequest.setMenuId(1L);
        orderLineItemRequest.setQuantity(2L);

        orderLineItemRequestList.add(orderLineItemRequest);

        orderRequest = new OrderRequest();
        orderRequest.setOrderStatus(OrderStatus.COOKING.name());
        orderRequest.setOrderTableId(1L);
        orderRequest.setOrderLineItems(orderLineItemRequestList);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void 주문_생성() throws Exception {
        final String jsonTypeOrder = objectMapper.writeValueAsString(orderRequest);

        mockMvc.perform(post(DEFAULT_ORDERS_URI)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonTypeOrder))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("orderTableId").value(orderRequest.getOrderTableId()))
            .andExpect(jsonPath("orderStatus").value(orderRequest.getOrderStatus()))
            .andExpect(jsonPath("orderLineItems[0].menuId").value(orderRequest.getOrderLineItems().get(0).getMenuId()))
            .andExpect(jsonPath("orderLineItems[0].quantity").value(orderRequest.getOrderLineItems().get(0).getQuantity()));
    }

    @DisplayName("주문을 조회한다.")
    @Test
    void 주문_조회() throws Exception {
        orderService.create(orderRequest);

        mockMvc.perform(get(DEFAULT_ORDERS_URI)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].orderTableId").value(orderRequest.getOrderTableId()))
            .andExpect(jsonPath("$[0].orderStatus").value(orderRequest.getOrderStatus()))
            .andExpect(jsonPath("$[0].orderLineItems[0].menuId").value(orderRequest.getOrderLineItems().get(0).getMenuId()))
            .andExpect(jsonPath("$[0].orderLineItems[0].orderId").exists())
            .andExpect(jsonPath("$[0].orderLineItems[0].quantity").value(orderRequest.getOrderLineItems().get(0).getQuantity()));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void 주문_상태_변경() throws Exception {
        final OrderResponse orderResponse = orderService.create(orderRequest);
        orderRequest.setOrderStatus(OrderStatus.MEAL.name());

        final String jsonTypeOrder = objectMapper.writeValueAsString(orderRequest);

        final String putRequestUri = DEFAULT_ORDERS_URI + orderResponse.getId() + "/order-status";

        mockMvc.perform(put(putRequestUri)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonTypeOrder))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(orderResponse.getId()))
            .andExpect(jsonPath("orderTableId").value(orderRequest.getOrderTableId()))
            .andExpect(jsonPath("orderStatus").value(orderRequest.getOrderStatus()))
            .andExpect(jsonPath("orderLineItems[0].menuId").value(orderRequest.getOrderLineItems().get(0).getMenuId()))
            .andExpect(jsonPath("orderLineItems[0].orderId").exists())
            .andExpect(jsonPath("orderLineItems[0].quantity").value(orderRequest.getOrderLineItems().get(0).getQuantity()));
    }
}
