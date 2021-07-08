package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.OrderService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.utils.domain.OrderLineItemObjects;
import kitchenpos.utils.domain.OrderObjects;
import kitchenpos.utils.domain.OrderTableObjects;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문 서비스")
class OrderServiceTest {
    @Mock
    private MenuDao menuDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;
    private Order order1;
    private Order order2;
    private Order changeOrder;
    private Order createOrder;
    private OrderLineItem orderLineItem1;
    private OrderLineItem orderLineItem2;
    private OrderLineItem orderLineItem3;
    private OrderLineItem orderLineItem4;
    private List<Order> orders;
    private List<OrderLineItem> orderLineItems1;
    private List<OrderLineItem> orderLineItems2;
    private OrderTable orderTable1;

    @BeforeEach
    void setUp() {
        OrderObjects orderObjects = new OrderObjects();
        OrderLineItemObjects orderLineItemObjects = new OrderLineItemObjects();
        OrderTableObjects orderTableObjects = new OrderTableObjects();
        order1 = orderObjects.getOrder1();
        order2 = orderObjects.getOrder2();
        changeOrder = orderObjects.getOrder3();
        createOrder = orderObjects.getOrder4();
        orders = new ArrayList<>(Arrays.asList(order1, order2));
        orderLineItem1 = orderLineItemObjects.getOrderLineItem1();
        orderLineItem2 = orderLineItemObjects.getOrderLineItem2();
        orderLineItem3 = orderLineItemObjects.getOrderLineItem3();
        orderLineItem4 = orderLineItemObjects.getOrderLineItem4();
        orderLineItems1 = new ArrayList<>(Arrays.asList(orderLineItem1, orderLineItem2));
        orderLineItems2 = new ArrayList<>(Arrays.asList(orderLineItem3, orderLineItem4));
        orderTable1 = orderTableObjects.getOrderTable1();
    }

    @TestFactory
    @DisplayName("모든 주문을 조회하는 기능")
    List<DynamicTest> find_allOrders() {
        // mocking
        when(orderDao.findAll()).thenReturn(orders);
        when(orderLineItemDao.findAllByOrderId(order1.getId())).thenReturn(orderLineItems1);
        when(orderLineItemDao.findAllByOrderId(order2.getId())).thenReturn(orderLineItems2);

        // when
        List<Order> findOrders = orderService.list();

        // then
        return Arrays.asList(
                dynamicTest("조회 결과 주문 ID 포함 확인됨.", () -> {
                    assertThat(findOrders)
                            .extracting("id")
                            .contains(order1.getId(), order2.getId());
                }),
                dynamicTest("주문 별 주문 항목 확인 됨.", () -> {
                    assertThat(findOrders.get(0).getOrderLineItems())
                            .extracting("seq")
                            .contains(orderLineItem1.getSeq(), orderLineItem2.getSeq());
                    assertThat(findOrders.get(1).getOrderLineItems())
                            .extracting("seq")
                            .contains(orderLineItem3.getSeq(), orderLineItem4.getSeq());
                })
        );
    }

    @TestFactory
    @DisplayName("주문 상태 변경 기능")
    List<DynamicTest> change_orderStatus() {
        // given
        changeOrder.setOrderStatus(OrderStatus.COOKING.name());

        // mocking
        when(orderDao.findById(anyLong())).thenReturn(Optional.of(changeOrder));
        when(orderDao.save(any(Order.class))).thenReturn(null);
        when(orderLineItemDao.findAllByOrderId(anyLong())).thenReturn(orderLineItems1);

        // when
        Order resultOrder = orderService.changeOrderStatus(1L, changeOrder);

        // then
        return Arrays.asList(
                dynamicTest("상태가 변경된 주문 ID 확인됨.", () -> {
                    assertThat(resultOrder.getId()).isEqualTo(changeOrder.getId());
                }),
                dynamicTest("변경 주문 상태 확인됨.", () -> {
                    assertThat(resultOrder.getOrderStatus()).isEqualTo(changeOrder.getOrderStatus());
                }),
                dynamicTest("주문 내역 확인됨.", () -> {
                    assertThat(resultOrder.getOrderLineItems())
                            .extracting("seq")
                            .contains(orderLineItem1.getSeq(), orderLineItem2.getSeq());
                })
        );
    }

    @TestFactory
    @DisplayName("주문 상태 변경 시 오류 발생 테스트")
    List<DynamicTest> change_exception() {
        return Arrays.asList(
                dynamicTest("수정 대상 조회 실패 시 오류 발생함.", () -> {
                    // given
                    when(orderDao.findById(anyLong())).thenReturn(Optional.empty());

                    // then
                    assertThatThrownBy(() -> orderService.changeOrderStatus(1L, changeOrder))
                            .isInstanceOf(IllegalArgumentException.class);
                }),
                dynamicTest("주분 상태가 완성일 경우 수정 요청 시 오류 발생함.", () -> {
                    // given
                    changeOrder.setOrderStatus(OrderStatus.COMPLETION.name());

                    // mocking
                    when(orderDao.findById(anyLong())).thenReturn(Optional.of(changeOrder));

                    // then
                    assertThatThrownBy(() -> orderService.changeOrderStatus(1L, changeOrder))
                            .isInstanceOf(IllegalArgumentException.class);
                })
        );
    }

    @TestFactory
    @DisplayName("신규 주문 등록 기능")
    List<DynamicTest> create_order() {
        // given
        createOrder.setOrderLineItems(orderLineItems1);
        orderTable1.setEmpty(false);

        // mocking
        when(menuDao.countByIdIn(any(List.class))).thenReturn(2L);
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable1));
        when(orderDao.save(any(Order.class))).thenReturn(createOrder);
        when(orderLineItemDao.save(orderLineItem1)).thenReturn(orderLineItem1);
        when(orderLineItemDao.save(orderLineItem2)).thenReturn(orderLineItem2);

        // when
        Order newOrder = orderService.create(createOrder);

        // then
        return Arrays.asList(
                dynamicTest("주문 ID 확인됨.", () -> assertThat(newOrder.getId()).isEqualTo(createOrder.getId())),
                dynamicTest("주문 상태 확인됨.", () -> assertThat(newOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())),
                dynamicTest("주문 테이블 확인됨.", () -> assertThat(newOrder.getOrderTableId()).isEqualTo(orderTable1.getId())),
                dynamicTest("주문 내역 확인됨.", () -> {
                    assertThat(newOrder.getOrderLineItems())
                            .extracting("seq")
                            .contains(orderLineItem1.getSeq(), orderLineItem2.getSeq());
                })
        );
    }

    @TestFactory
    @DisplayName("주문 등록 요청 시 예외상황 발생 테스트")
    List<DynamicTest> create_order_exception() {

        return Arrays.asList(
                dynamicTest("주문 내역 누락 시 오류 발생됨.", () -> {
                    // given
                    createOrder.setOrderLineItems(null);

                    // then
                    assertThatThrownBy(() -> orderService.create(createOrder))
                            .isInstanceOf(IllegalArgumentException.class);
                }),
                dynamicTest("주문 내역 메뉴에 등록되지 않은 메뉴가 포함되어 있을 경우 오류 발생됨.", () -> {
                    // given
                    createOrder.setOrderLineItems(orderLineItems1);

                    // mocking
                    when(menuDao.countByIdIn(any(List.class))).thenReturn(1L);

                    // then
                    assertThatThrownBy(() -> orderService.create(createOrder))
                            .isInstanceOf(IllegalArgumentException.class);
                }),
                dynamicTest("주문 테이블이 누락되었을 경우 오류 발생됨.", () -> {
                    // given
                    createOrder.setOrderLineItems(orderLineItems1);

                    // mocking
                    when(menuDao.countByIdIn(any(List.class))).thenReturn(2L);
                    when(orderTableDao.findById(anyLong())).thenReturn(Optional.empty());

                    // then
                    assertThatThrownBy(() -> orderService.create(createOrder))
                            .isInstanceOf(IllegalArgumentException.class);
                }),
                dynamicTest("주문한 테이블이 비어있을 경우 오류 발생됨.", () -> {
                    // given
                    createOrder.setOrderLineItems(orderLineItems1);
                    orderTable1.setEmpty(true);

                    // mocking
                    when(menuDao.countByIdIn(any(List.class))).thenReturn(2L);
                    when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable1));

                    // then
                    assertThatThrownBy(() -> orderService.create(createOrder))
                            .isInstanceOf(IllegalArgumentException.class);
                })
        );
    }
}
