package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusRequest;
import kitchenpos.factory.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static kitchenpos.factory.MenuFixtureFactory.*;
import static kitchenpos.factory.MenuGroupFixtureFactory.*;
import static kitchenpos.factory.MenuProductFixtureFactory.*;
import static kitchenpos.factory.OrderFixtureFactory.*;
import static kitchenpos.factory.OrderLineItemFixtureFactory.*;
import static kitchenpos.factory.OrderTableFixtureFactory.*;
import static kitchenpos.factory.ProductFixtureFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    MenuDao menuDao;
    @Mock
    OrderDao orderDao;
    @Mock
    OrderLineItemDao orderLineItemDao;
    @Mock
    OrderTableDao orderTableDao;
    @InjectMocks
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
        메뉴그룹_한식 = createMenuGroup(1L, "한식메뉴");
        김치찌개 = createProduct(1L, "김치찌개", BigDecimal.valueOf(8000L));
        공기밥 = createProduct(2L, "공기밥", BigDecimal.valueOf(1000L));
        메뉴_김치찌개세트 = createMenu("김치찌개세트", BigDecimal.valueOf(15000L), 메뉴그룹_한식.getId());

        김치찌개세트_김치찌개 = createMenuProduct(1L, 메뉴_김치찌개세트.getId(), 김치찌개.getId(), 2);
        김치찌개세트_공기밥 = createMenuProduct(1L, 메뉴_김치찌개세트.getId(), 공기밥.getId(), 2);
        메뉴_김치찌개세트.setMenuProducts(Arrays.asList(김치찌개세트_김치찌개, 김치찌개세트_공기밥));

        테이블_1 = createOrderTable(1L, null, 4, false);
        접수된_주문 = createOrder(1L, 테이블_1.getId(), OrderStatus.COOKING.name(), LocalDateTime.now());
        접수된주문_김치찌개세트 = createOrderLineItem(1L, 접수된_주문.getId(), 메뉴_김치찌개세트.getId(), 1);
        접수된_주문.setOrderLineItems(Arrays.asList(접수된주문_김치찌개세트));

        테이블_2 = createOrderTable(2L, null, 4, false);
        완료된_주문 = createOrder(2L, 테이블_2.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now());
        완료된주문_김치찌개세트 = createOrderLineItem(2L, 접수된_주문.getId(), 메뉴_김치찌개세트.getId(), 1);
        완료된_주문.setOrderLineItems(Arrays.asList(완료된주문_김치찌개세트));

        테이블_EMPTY = createOrderTable(3L, null, 0, true);
    }

    @DisplayName("주문을 등록할 수 있다")
    @Test
    void 주문_등록(){
        //given
        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(orderTableDao.findById(테이블_1.getId())).willReturn(Optional.of(테이블_1));
        given(orderDao.save(any(Order.class))).willReturn(접수된_주문);
        OrderRequest 접수된_주문_request = OrderRequest.of(
                접수된_주문.getOrderTableId(),
                접수된_주문.getOrderLineItems().stream()
                        .map(orderLineItem -> OrderLineItemRequest.of(orderLineItem.getMenuId(), (int) orderLineItem.getQuantity()))
                        .collect(Collectors.toList())
        );

        //when
        OrderResponse savedOrder = orderService.create(접수된_주문_request);

        //then
        assertThat(savedOrder).isEqualTo(OrderResponse.from(접수된_주문));
    }

    @DisplayName("주문항목은 비어있을 수 없다")
    @Test
    void 주문_주문항목_검증(){
        //given
        OrderRequest invalidRequest = OrderRequest.of(테이블_1.getId(), new ArrayList<>());
        //then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(invalidRequest));
    }

    @DisplayName("주문항목간 메뉴는 중복될 수 없다")
    @Test
    void 주문_주문항목_메뉴_중복_검증(){
        //given
        OrderLineItemRequest 중복_메뉴 = OrderLineItemRequest.of(접수된주문_김치찌개세트.getMenuId(),
                (int) 접수된주문_김치찌개세트.getQuantity());
        OrderRequest invalidRequest = OrderRequest.of(접수된_주문.getId(), Arrays.asList(중복_메뉴, 중복_메뉴));

        //then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(invalidRequest));
    }

    @DisplayName("등록하려는 주문테이블이 존재해야 한다")
    @Test
    void 주문_주문테이블_검증(){
        //given
        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(orderTableDao.findById(테이블_1.getId())).willReturn(Optional.ofNullable(null));
        OrderRequest 접수된_주문_request = OrderRequest.of(
                접수된_주문.getOrderTableId(),
                접수된_주문.getOrderLineItems().stream()
                        .map(orderLineItem -> OrderLineItemRequest.of(orderLineItem.getMenuId(), (int) orderLineItem.getQuantity()))
                        .collect(Collectors.toList())
        );

        //then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(접수된_주문_request));
    }

    @DisplayName("등록하려는 주문테이블은 비어있을 수 없다")
    @Test
    void 주문_주문테이블_Empty_검증(){
        //given
        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(orderTableDao.findById(테이블_EMPTY.getId())).willReturn(Optional.of(테이블_EMPTY));
        OrderRequest invalidOrder = OrderRequest.of(
                테이블_EMPTY.getId(),
                Arrays.asList(OrderLineItemRequest.of(메뉴_김치찌개세트.getId(), 1))
        );

        //then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(invalidOrder));
    }

    @DisplayName("등록하려는 주문항목의 메뉴가 존재해야 한다")
    @Test
    void 주문_주문항목_메뉴_검증(){
        //given
        given(menuDao.countByIdIn(anyList())).willReturn(2L);
        OrderRequest 접수된_주문_request = OrderRequest.of(
                접수된_주문.getOrderTableId(),
                접수된_주문.getOrderLineItems().stream()
                        .map(orderLineItem -> OrderLineItemRequest.of(orderLineItem.getMenuId(), (int) orderLineItem.getQuantity()))
                        .collect(Collectors.toList())
        );

        //then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(접수된_주문_request));
    }

    @DisplayName("주문의 목록을 조회할 수 있다")
    @Test
    void 주문_목록_조회(){
        //given
        given(orderDao.findAll()).willReturn(Arrays.asList(접수된_주문));

        //when
        List<OrderResponse> list = orderService.list();

        //then
        assertThat(list).containsExactly(OrderResponse.from(접수된_주문));
    }

    @DisplayName("주문의 상태를 업데이트할 수 있다")
    @ParameterizedTest(name = "이전 주문상태: {0}, 새로운 주문상태: {1}")
    @MethodSource("provideParametersForOrderStateUpdate")
    void 주문_상태_업데이트(String beforeStatus, String afterStatus){
        //given
        Order order = createOrder(3L, 테이블_1.getId(), beforeStatus, LocalDateTime.now());
        given(orderDao.findById(anyLong())).willReturn(Optional.of(order));

        //when
        OrderStatusRequest newStatus = OrderStatusRequest.from(afterStatus);
        OrderResponse changedOrder = orderService.changeOrderStatus(order.getId(), newStatus);

        //then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(afterStatus);
    }

    private static Stream<Arguments> provideParametersForOrderStateUpdate() {
        return Stream.of(
                Arguments.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()),
                Arguments.of(OrderStatus.MEAL.name(), OrderStatus.COMPLETION.name())
        );
    }

    @DisplayName("주문이 존재해야 주문상태를 업데이트할 수 있다")
    @Test
    void 주문_상태_업데이트_주문_검증(){
        //given
        given(orderDao.findById(anyLong())).willReturn(Optional.ofNullable(null));

        //when
        OrderStatusRequest status_MEAL = OrderStatusRequest.from(OrderStatus.MEAL.name());

        //then
        assertThrows(IllegalArgumentException.class, () -> orderService.changeOrderStatus(접수된_주문.getId(), status_MEAL));
    }

    @DisplayName("주문상태가 COMPLETION이면 주문상태를 업데이트할 수 없다")
    @Test
    void 주문_상태_업데이트_COMPLETION_검증(){
        //given
        given(orderDao.findById(anyLong())).willReturn(Optional.of(완료된_주문));

        //when
        OrderStatusRequest status_MEAL = OrderStatusRequest.from(OrderStatus.MEAL.name());

        //then
        assertThrows(IllegalArgumentException.class, () -> orderService.changeOrderStatus(완료된_주문.getId(), status_MEAL));
    }
}