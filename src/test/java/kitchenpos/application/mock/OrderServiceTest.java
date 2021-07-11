package kitchenpos.application.mock;

import kitchenpos.order.application.OrderService;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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
    @InjectMocks
    private OrderService orderService;

    private OrderLineItem givenOrderLineItem = new OrderLineItem();
    private Order givenOrder = new Order();

    @BeforeEach
    void setUp() {
        orderLineItemSetup();
        orderSetup();
    }

    private void orderSetup() {
        givenOrder.setId(1L);
        givenOrder.setOrderStatus("COOKING");
        givenOrder.setOrderTableId(1L);
        givenOrder.setOrderedTime(LocalDateTime.parse("2021-07-04T12:00:00"));
        givenOrder.setOrderLineItems(Arrays.asList(givenOrderLineItem));
    }

    private void orderLineItemSetup() {
        givenOrderLineItem.setOrderId(1L);
        givenOrderLineItem.setSeq(1L);
        givenOrderLineItem.setMenuId(1L);
        givenOrderLineItem.setQuantity(1L);
    }

    @DisplayName("주문 항목이 없으면 주문할 수 없다.")
    @Test
    void createFailBecauseOfWrongProductTest() {
        //given
        givenOrder.setOrderLineItems(new ArrayList<>());

        //when && then
        assertThatThrownBy(() -> orderService.create(givenOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 항목이 비어있습니다.");

    }

    @DisplayName("등록된 메뉴만 주문 할 수 있다.")
    @Test
    void createFailBecauseOfNotExistMenuTest() {
        //given
        given(menuDao.countByIdIn(any())).willReturn(0L);

        //when && then
        assertThatThrownBy(() -> orderService.create(givenOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록되지 않은 메뉴가 있습니다.");

    }

    @DisplayName("주문 테이블이 존재해야 한다.")
    @Test
    void createFailBecauseOfNotExistTableTest() {
        //given
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableDao.findById(any())).willReturn(Optional.empty());

        //when && then
        assertThatThrownBy(() -> orderService.create(givenOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록되지 않은 주문 테이블입니다.");

    }

    @DisplayName("주문 테이블은 비어있지 않아야 한다.")
    @Test
    void createFailBecauseOfEmptyTableTest() {
        //given
        given(menuDao.countByIdIn(any())).willReturn(1L);
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));

        //when && then
        assertThatThrownBy(() -> orderService.create(givenOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 테이블은 주문 할 수 없습니다.");

    }

    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void createTest() {
        //given
        given(menuDao.countByIdIn(any())).willReturn(1L);
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        given(orderDao.save(givenOrder)).willReturn(givenOrder);

        //when
        orderService.create(givenOrder);

        //then
        verify(orderDao).save(givenOrder);
        verify(orderLineItemDao).save(givenOrderLineItem);
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() {
        //given
        List<Order> expect = Arrays.asList(givenOrder);
        given(orderDao.findAll())
                .willReturn(expect);

        //when
        List<Order> result = orderService.list();

        //then
        verify(orderDao).findAll();
        assertThat(result.size()).isEqualTo(expect.size());
        assertThat(result).containsExactly(givenOrder);
    }

    @DisplayName("등록되지 않은 주문은 변경할 수 없다.")
    @Test
    void changeOrderStatusFailBecauseOfNotExistOrderTest() {
        //given
        given(orderDao.findById(givenOrder.getId())).willReturn(Optional.empty());

        //when && then
        assertThatThrownBy(() -> orderService.changeOrderStatus(givenOrder.getId(), givenOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록되지 않은 주문입니다.");

    }

    @DisplayName("완료된 주문은 변경할 수 없다")
    @Test
    void changeOrderStatusFailBecauseOfOrderStatusTest() {
        //given
        givenOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(givenOrder.getId())).willReturn(Optional.ofNullable(givenOrder));

        //when && then
        assertThatThrownBy(() -> orderService.changeOrderStatus(givenOrder.getId(), givenOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 완료된 주문입니다.");

    }

    @DisplayName("주문 상태를 변경할 수 있다")
    @Test
    void changeOrderStatusTest() {
        //given
        given(orderDao.findById(givenOrder.getId())).willReturn(Optional.ofNullable(givenOrder));
        Order modifiedOrder = createModifiedOrder();

        //when
        Order result = orderService.changeOrderStatus(givenOrder.getId(), modifiedOrder);

        //
        verify(orderDao).save(givenOrder);
        assertThat(result.getOrderStatus()).isEqualTo(modifiedOrder.getOrderStatus());

    }

    private Order createModifiedOrder() {
        Order modifiedOrder = new Order();
        modifiedOrder.setId(givenOrder.getId());
        modifiedOrder.setOrderStatus("MEAL");
        modifiedOrder.setOrderTableId(givenOrder.getOrderTableId());
        modifiedOrder.setOrderedTime(givenOrder.getOrderedTime());
        modifiedOrder.setOrderLineItems(givenOrder.getOrderLineItems());
        return modifiedOrder;
    }


}