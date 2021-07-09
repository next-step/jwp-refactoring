package kitchenpos.application;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private Order order;
    private Order order2;
    private OrderLineItem orderLineItem;
    private OrderLineItem orderLineItem2;
    private OrderTable orderTable;

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

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);
        order.setOrderTableId(1L);

        order2 = new Order();
        order2.setId(2L);
        order2.setOrderTableId(1L);

        orderLineItem = new OrderLineItem();
        orderLineItem2 = new OrderLineItem();

        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setTableGroupId(1L);
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);
    }

    @DisplayName("주문을 등록한다")
    @Test
    void create() {
        // given
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem);
        given(menuDao.countByIdIn(any())).willReturn((long)orderLineItems.size());
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));
        given(orderDao.save(order)).willReturn(order);

        // when
        order.setOrderLineItems(orderLineItems);

        Order actual = orderService.create(order);

        // then
        assertThat(actual).isEqualTo(order);
    }

    @DisplayName("주문을 등록한다 - 메뉴 정보는 필수 입력사항이다")
    @Test
    void create_menuIsEssentialInput() {
        // given when
        order.setOrderLineItems(Collections.emptyList());

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("주문을 등록한다 - 테이블 정보는 필수 입력사항이다")
    @Test
    void create_tableIsEssentialInput() {
        // given
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem);
        orderLineItems.add(orderLineItem2);
        given(menuDao.countByIdIn(any())).willReturn((long)orderLineItems.size());
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.empty());

        // when
        order.setOrderLineItems(orderLineItems);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("주문을 등록한다 - 동일한 메뉴정보는 중복 입력할 수 없다")
    @Test
    void create_duplicateMenu() {
        // given
        given(menuDao.countByIdIn(any())).willReturn(1L);

        // when
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem);
        orderLineItems.add(orderLineItem2);
        order.setOrderLineItems(orderLineItems);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("전체 주문 목록을 조회한다")
    @Test
    void list() {
        // given
        given(orderDao.findAll()).willReturn(Arrays.asList(order, order2));
        given(orderLineItemDao.findAllByOrderId(any())).willReturn(new ArrayList<>());

        // when
        List<Order> actual = orderService.list();

        // then
        assertThat(actual).isEqualTo(Arrays.asList(order, order2));
    }

    @DisplayName("주문의 상태를 변경한다")
    @Test
    void changeOrderStatus() {
        // given
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));
        given(orderLineItemDao.findAllByOrderId(any())).willReturn(new ArrayList<>());

        // when
        Order orderParam = new Order();
        orderParam.setOrderStatus(OrderStatus.COOKING.name());
        Order actual = orderService.changeOrderStatus(order.getId(), orderParam);

        // then
        assertThat(actual).isEqualTo(order);
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @DisplayName("주문 상태변경 - 주문의 상태가 '계산완료' 인 경우 상태를 변경할 수 없다")
    @Test
    void changeOrderStatus_illegalState() {
        // given
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));
        order.setOrderStatus(OrderStatus.COMPLETION.name());

        // when
        Order orderParam = new Order();
        orderParam.setOrderStatus(OrderStatus.COOKING.name());

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(order.getId(), orderParam));
    }

}