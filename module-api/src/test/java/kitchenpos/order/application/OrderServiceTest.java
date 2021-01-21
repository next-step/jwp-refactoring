package kitchenpos.order.application;

import kitchenpos.menu.*;
import kitchenpos.menugroup.MenuGroup;
import kitchenpos.menugroup.MenuGroupRepository;
import kitchenpos.order.OrderService;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.Product;
import kitchenpos.product.ProductRepository;
import kitchenpos.table.OrderTable;
import kitchenpos.table.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 비즈니스 로직을 처리하는 서비스 테스트")
class OrderServiceTest extends BaseTest {
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

    @Autowired
    private OrderService orderService;

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
    void 주문_생성() {
        final OrderResponse savedOrder = orderService.create(orderRequest);

        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderTableId()).isEqualTo(orderRequest.getOrderTableId());
        assertThat(savedOrder.getOrderStatus().name()).isEqualTo(orderRequest.getOrderStatus());

        assertThat(savedOrder.getOrderLineItems().size()).isEqualTo(1);
        assertThat(savedOrder.getOrderLineItems().get(0).getOrderId()).isNotNull();
        assertThat(savedOrder.getOrderLineItems().get(0).getMenuId()).isEqualTo(1);
        assertThat(savedOrder.getOrderLineItems().get(0).getQuantity()).isEqualTo(2);
    }

    @DisplayName("상품을 주문하지 않는 경우 주문을 생성할 수 없다.")
    @Test
    void 상품을_주문하지_않는_경우_주문_생성() {
        assertThatThrownBy(() -> orderRequest.setOrderLineItems(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 등록되지 않은 상품을 주문하는 경우 주문을 생성할 수 없다.")
    @Test
    void 등록되지_않은_상품을_주문하는_경우_주문_생성() {
        final List<OrderLineItemRequest> invalidOrderLineItemRequestList = new ArrayList<>();

        OrderLineItemRequest invalidOrderLineItemRequest = new OrderLineItemRequest();
        invalidOrderLineItemRequest.setMenuId(121L);
        invalidOrderLineItemRequest.setQuantity(2L);

        invalidOrderLineItemRequestList.add(invalidOrderLineItemRequest);

        orderRequest.setOrderLineItems(invalidOrderLineItemRequestList);

        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는(비어있는) 주문 테이블을 주문하는 경우 주문을 생성할 수 없다.")
    @Test
    void 존재하지_않는_주문_테이블을_주문하는_경우_주문_생성() {
        orderRequest.setOrderTableId(1829L);

        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 조회한다.")
    @Test
    void 주문_조회() {
        orderService.create(orderRequest);

        final List<OrderResponse> orderResponseList = orderService.getOrderList();

        assertThat(orderResponseList.size()).isGreaterThan(0);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void 주문_상태_변경() {
        final OrderResponse orderResponse = orderService.create(orderRequest);

        orderRequest.setOrderStatus(OrderStatus.MEAL.name());

        final OrderResponse changedOrder = orderService.changeOrderStatus(orderResponse.getId(), orderRequest);
        assertThat(changedOrder.getOrderStatus().name()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("없는 주문의 경우 상태를 변경할 수 없다..")
    @Test
    void 주문이_존재하지_않는_경우_주문_상태_변경() {
        orderRequest.setOrderStatus(OrderStatus.MEAL.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(182891L, orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 완료된 상태인 경우 주문의 상태를 변경할 수 없다.")
    @Test
    void 주문이_완료된_상태인_경우_주문_상태_변경() {
        final OrderResponse orderResponse = orderService.create(orderRequest);

        orderRequest.setOrderStatus(OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(orderResponse.getId(), orderRequest);

        orderRequest.setOrderStatus(OrderStatus.MEAL.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(orderResponse.getId(), orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
