package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 관련 Service 단위 테스트 - Stub")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuDao menuDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        //given
        OrderLineItem orderLineItem1 = new OrderLineItem(null, null, 1L, 10);
        OrderLineItem orderLineItem2 = new OrderLineItem(null, null, 3L, 5);
        OrderTable orderTable = new OrderTable(3L, null, 3, false);
        Order request = new Order(null, orderTable.getId(), null, null, Arrays.asList(orderLineItem1, orderLineItem2));
        long registeredMenuCount = 2;
        long generateNewOrderId = 5;

        given(menuDao.countByIdIn(Arrays.asList(orderLineItem1.getMenuId(), orderLineItem2.getMenuId())))
                .willReturn(registeredMenuCount);
        given(orderTableDao.findById(request.getOrderTableId())).willReturn(Optional.of(orderTable));
        doAnswer(invocation -> new Order(generateNewOrderId, request.getOrderTableId(), request.getOrderStatus(),
                request.getOrderedTime(), Arrays.asList(orderLineItem1, orderLineItem2)))
                .when(orderDao).save(request);
        given(orderLineItemDao.save(orderLineItem1)).willReturn(orderLineItem1);
        given(orderLineItemDao.save(orderLineItem2)).willReturn(orderLineItem2);

        //when
        Order result = orderService.create(request);

        //then
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(result.getOrderTableId()).isEqualTo(orderTable.getId());
        assertThat(result.getOrderLineItems().get(0).getOrderId()).isEqualTo(generateNewOrderId);
        assertThat(result.getOrderLineItems().get(1).getOrderId()).isEqualTo(generateNewOrderId);
    }

    @DisplayName("주문 항목이 없거나 null이면 주문을 등록 할 수 없다.")
    @Test
    void create_empty_or_null() {
        //given
        Order emptyRequest = new Order(null, 1L, null, null, Collections.emptyList());
        Order nullRequest = new Order(null, 1L, null, null, null);

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(emptyRequest));
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(nullRequest));
    }

    @DisplayName("등록 되어있지 않은 메뉴가 있는 경우 주문을 등록 할 수 없다.")
    @Test
    void create_not_registered_menu() {
        //given
        OrderLineItem orderLineItem1 = new OrderLineItem(null, null, 1L, 10);
        OrderLineItem orderLineItem2 = new OrderLineItem(null, null, 9999999L, 5);
        Order request = new Order(null, 1L, null, null, Arrays.asList(orderLineItem1, orderLineItem2));
        long registeredMenuCount = 1;

        given(menuDao.countByIdIn(Arrays.asList(orderLineItem1.getMenuId(), orderLineItem2.getMenuId())))
                .willReturn(registeredMenuCount);

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("빈 테이블인 경우 주문을 등록 할 수 없다.")
    @Test
    void create_empty_table() {
        //given
        OrderLineItem orderLineItem1 = new OrderLineItem(null, null, 1L, 10);
        OrderLineItem orderLineItem2 = new OrderLineItem(null, null, 2L, 5);
        OrderTable emptyTable = new OrderTable(3L, null, 0, true);
        Order request = new Order(null, emptyTable.getId(), null, null, Arrays.asList(orderLineItem1, orderLineItem2));
        long registeredMenuCount = 2;

        given(menuDao.countByIdIn(Arrays.asList(orderLineItem1.getMenuId(), orderLineItem2.getMenuId())))
                .willReturn(registeredMenuCount);
        given(orderTableDao.findById(request.getOrderTableId())).willReturn(Optional.of(emptyTable));

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("주문 테이블 없는 경우 주문을 등록 할 수 없다.")
    @Test
    void create_not_exist_order_table() {
        //given
        OrderLineItem orderLineItem1 = new OrderLineItem(null, null, 1L, 10);
        OrderLineItem orderLineItem2 = new OrderLineItem(null, null, 2L, 5);
        Order request = new Order(null, 99999L, null, null, Arrays.asList(orderLineItem1, orderLineItem2));
        long registeredMenuCount = 2;

        given(menuDao.countByIdIn(Arrays.asList(orderLineItem1.getMenuId(), orderLineItem2.getMenuId())))
                .willReturn(registeredMenuCount);
        given(orderTableDao.findById(request.getOrderTableId())).willReturn(Optional.empty());

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("주문 상태를 업데이트 한다.")
    @Test
    void changeOrderStatus() {
        //given
        long requestOrderId = 1;
        Order request = new Order(null, null, OrderStatus.MEAL.name(), null, null);
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 1L, 10);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1L, 3L, 5);

        given(orderDao.findById(requestOrderId))
                .willReturn(Optional.of(new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), null)));
        given(orderLineItemDao.findAllByOrderId(requestOrderId))
                .willReturn(Arrays.asList(orderLineItem1, orderLineItem2));

        //when
        Order result = orderService.changeOrderStatus(requestOrderId, request);

        //then
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }


    @DisplayName("주문 상태가 계산완료인 경우 주문 상태를 업데이트 할 수 없다.")
    @Test
    void changeOrderStatus_completion() {
        //given
        long requestOrderId = 1;
        Order request = new Order(null, null, OrderStatus.MEAL.name(), null, null);

        given(orderDao.findById(requestOrderId))
                .willReturn(Optional.of(new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), null)));

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(requestOrderId, request));
    }


    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        //given
        OrderLineItem orderLineItem1 = new OrderLineItem(null, null, 1L, 10);
        OrderLineItem orderLineItem2 = new OrderLineItem(null, null, 3L, 5);
        OrderLineItem orderLineItem3 = new OrderLineItem(null, null, 1L, 10);
        Order order1 = new Order(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), null);
        Order order2 = new Order(2L, 2L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), null);

        given(orderDao.findAll()).willReturn(Arrays.asList(order1, order2));
        given(orderLineItemDao.findAllByOrderId(order1.getId()))
                .willReturn(Arrays.asList(orderLineItem1, orderLineItem2));
        given(orderLineItemDao.findAllByOrderId(order2.getId())).willReturn(Arrays.asList(orderLineItem3));

        //when
        List<Order> results = orderService.list();

        //then
        assertThat(results.get(0).getOrderLineItems()).hasSize(2);
        assertThat(results.get(1).getOrderLineItems()).hasSize(1);
    }

}
