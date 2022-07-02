package kitchenpos.order.application;

import kitchenpos.exception.*;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static kitchenpos.utils.fixture.MenuFixtureFactory.createMenu;
import static kitchenpos.utils.fixture.MenuGroupFixtureFactory.createMenuGroup;
import static kitchenpos.utils.fixture.MenuProductFixtureFactory.createMenuProduct;
import static kitchenpos.utils.fixture.OrderFixtureFactory.createOrder;
import static kitchenpos.utils.fixture.OrderLineItemFixtureFactory.createOrderLineItem;
import static kitchenpos.utils.fixture.OrderTableFixtureFactory.createOrderTable;
import static kitchenpos.utils.fixture.ProductFixtureFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@DisplayName("주문 Service 테스트")
class OrderServiceTest {
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    MenuGroupRepository menuGroupRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    MenuProductRepository menuProductRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderLineItemRepository orderLineItemRepository;
    @Autowired
    OrderTableRepository orderTableRepository;
    @Autowired
    OrderService orderService;


    private MenuGroup 메뉴그룹_한식;
    private Product 김치찌개;
    private Product 공기밥;
    private Menu 메뉴_김치찌개세트;
    private MenuProduct 김치찌개세트_김치찌개;
    private MenuProduct 김치찌개세트_공기밥;
    private OrderTable 테이블_1;
    private OrderTable 테이블_2;
    private OrderTable 테이블_EMPTY;
    private Order 접수된_주문;
    private Order 완료된_주문;
    private OrderLineItem 접수된주문_김치찌개세트;
    private OrderLineItem 완료된주문_김치찌개세트;

    @BeforeEach
    void setUp() {
        메뉴그룹_한식 = menuGroupRepository.save(createMenuGroup("한식메뉴"));
        김치찌개 = productRepository.save(createProduct("김치찌개", 8000));
        공기밥 = productRepository.save(createProduct("공기밥", 1000));
        김치찌개세트_김치찌개 = createMenuProduct(김치찌개, 2);
        김치찌개세트_공기밥 = createMenuProduct(공기밥, 2);
        메뉴_김치찌개세트 = menuRepository.save(createMenu("김치찌개세트", 15000, 메뉴그룹_한식
                , Arrays.asList(김치찌개세트_김치찌개, 김치찌개세트_공기밥)));

        테이블_1 = orderTableRepository.save(createOrderTable(4, false));
        접수된_주문 = createOrder(테이블_1, LocalDateTime.now());
        접수된주문_김치찌개세트 = createOrderLineItem(접수된_주문, 메뉴_김치찌개세트, 1);
        접수된_주문.registerOrderLineItems(Arrays.asList(접수된주문_김치찌개세트));

        테이블_2 = orderTableRepository.save(createOrderTable(4, false));
        완료된_주문 = createOrder(테이블_2, LocalDateTime.now());
        완료된_주문.changeStatus(OrderStatus.COMPLETION);
        완료된주문_김치찌개세트 = createOrderLineItem(접수된_주문, 메뉴_김치찌개세트, 1);
        완료된_주문.registerOrderLineItems(Arrays.asList(완료된주문_김치찌개세트));

        테이블_EMPTY = orderTableRepository.save(createOrderTable(0, true));
    }

    @DisplayName("주문을 등록할 수 있다")
    @Test
    void 주문_등록(){
        //given
        OrderRequest 접수된_주문_request = OrderRequest.of(
                접수된_주문.getOrderTable().getId(),
                접수된_주문.getOrderLineItems().stream()
                        .map(orderLineItem -> OrderLineItemRequest.of(orderLineItem.getMenu().getId(),
                                (int) orderLineItem.getQuantity()))
                        .collect(Collectors.toList())
        );

        //when
        OrderResponse savedOrder = orderService.create(접수된_주문_request);

        //then
        assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder.getOrderLineItems().size()).isEqualTo(1)
        );
    }

    @DisplayName("주문항목은 비어있을 수 없다")
    @Test
    void 주문_주문항목_검증(){
        //given
        OrderRequest invalidRequest = OrderRequest.of(테이블_1.getId(), new ArrayList<>());
        //then
        assertThrows(IllegalOrderLineItemException.class, () -> orderService.create(invalidRequest));
    }

    @DisplayName("주문항목간 메뉴는 중복될 수 없다")
    @Test
    void 주문_주문항목_메뉴_중복_검증(){
        //given
        OrderLineItemRequest 중복_메뉴 = OrderLineItemRequest.of(접수된주문_김치찌개세트.getMenu().getId(),
                (int) 접수된주문_김치찌개세트.getQuantity());
        OrderRequest invalidRequest = OrderRequest.of(접수된_주문.getOrderTable().getId(),
                Arrays.asList(중복_메뉴, 중복_메뉴));

        //then
        assertThrows(IllegalOrderLineItemException.class, () -> orderService.create(invalidRequest));
    }

    @DisplayName("등록하려는 주문테이블이 존재해야 한다")
    @Test
    void 주문_주문테이블_검증(){
        //given
        OrderRequest 접수된_주문_request = OrderRequest.of(
                0L,
                접수된_주문.getOrderLineItems().stream()
                        .map(orderLineItem -> OrderLineItemRequest.of(orderLineItem.getMenu().getId(),
                                (int) orderLineItem.getQuantity()))
                        .collect(Collectors.toList())
        );

        //then
        assertThrows(NoSuchOrderTableException.class, () -> orderService.create(접수된_주문_request));
    }

    @DisplayName("등록하려는 주문테이블은 비어있을 수 없다")
    @Test
    void 주문_주문테이블_Empty_검증(){
        //given
        OrderRequest invalidOrder = OrderRequest.of(
                테이블_EMPTY.getId(),
                Arrays.asList(OrderLineItemRequest.of(메뉴_김치찌개세트.getId(), 1))
        );

        //then
        assertThrows(IllegalOrderTableException.class, () -> orderService.create(invalidOrder));
    }

    @DisplayName("등록하려는 주문항목의 메뉴가 존재해야 한다")
    @Test
    void 주문_주문항목_메뉴_검증(){
        //given
        OrderRequest 접수된_주문_request = OrderRequest.of(
                접수된_주문.getOrderTable().getId(),
                접수된_주문.getOrderLineItems().stream()
                        .map(orderLineItem -> OrderLineItemRequest.of(0L,
                                (int) orderLineItem.getQuantity()))
                        .collect(Collectors.toList())
        );

        //then
        assertThrows(NoSuchMenuException.class, () -> orderService.create(접수된_주문_request));
    }

    @DisplayName("주문의 목록을 조회할 수 있다")
    @Test
    void 주문_목록_조회(){
        //given
        OrderRequest 접수된_주문_request = OrderRequest.of(
                접수된_주문.getOrderTable().getId(),
                접수된_주문.getOrderLineItems().stream()
                        .map(orderLineItem -> OrderLineItemRequest.of(orderLineItem.getMenu().getId(),
                                (int) orderLineItem.getQuantity()))
                        .collect(Collectors.toList())
        );
        OrderResponse savedOrder = orderService.create(접수된_주문_request);

        //when
        List<OrderResponse> list = orderService.list();

        //then
        assertThat(list).contains(savedOrder);
    }

    @DisplayName("주문의 상태를 업데이트할 수 있다")
    @ParameterizedTest(name = "이전 주문상태: {0}, 새로운 주문상태: {1}")
    @MethodSource("provideParametersForOrderStateUpdate")
    void 주문_상태_업데이트(OrderStatus beforeStatus, OrderStatus afterStatus){
        //given
        Order order = createOrder(테이블_1, LocalDateTime.now());
        order.changeStatus(beforeStatus);
        orderRepository.save(order);
        order.registerOrderLineItems(Arrays.asList(접수된주문_김치찌개세트));

        //when
        OrderStatusRequest newStatus = OrderStatusRequest.from(afterStatus);
        OrderResponse changedOrder = orderService.changeOrderStatus(order.getId(), newStatus);

        //then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(afterStatus);
    }

    private static Stream<Arguments> provideParametersForOrderStateUpdate() {
        return Stream.of(
                Arguments.of(OrderStatus.COOKING, OrderStatus.MEAL),
                Arguments.of(OrderStatus.MEAL, OrderStatus.COMPLETION)
        );
    }

    @DisplayName("주문이 존재해야 주문상태를 업데이트할 수 있다")
    @Test
    void 주문_상태_업데이트_주문_검증(){
        //given
        OrderStatusRequest status_MEAL = OrderStatusRequest.from(OrderStatus.MEAL);

        //then
        assertThrows(NoSuchOrderException.class, () -> orderService.changeOrderStatus(0L, status_MEAL));
    }

    @DisplayName("주문상태가 COMPLETION이면 주문상태를 업데이트할 수 없다")
    @Test
    void 주문_상태_업데이트_COMPLETION_검증(){
        //given
        Order order = createOrder(테이블_1, LocalDateTime.now());
        order.changeStatus(OrderStatus.COMPLETION);
        orderRepository.save(order);

        //when
        OrderStatusRequest status_MEAL = OrderStatusRequest.from(OrderStatus.MEAL);

        //then
        assertThrows(IllegalOrderException.class, () -> orderService.changeOrderStatus(order.getId(), status_MEAL));
    }
}