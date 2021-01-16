package kitchenpos.application;

import kitchenpos.common.DefaultData;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderServiceUnitTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문 항목 개수를 0개로 등록한다")
    @Test
    void testCreateOrderWithZeroLineItem() {
        // given
        Order order = new Order();

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("주문 항목에 존재하지 않는 메뉴를 같이 등록한다")
    @Test
    void testCreateOrderWithDuplicatedMenu() {
        // given
        OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setMenuId(DefaultData.메뉴_후라이드치킨_ID);

        OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setMenuId(0L);

        Order order = new Order();
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

        List<Long> menuIds = Arrays.asList(DefaultData.메뉴_후라이드치킨_ID, 0L);

        given(menuDao.countByIdIn(menuIds)).willReturn(1L);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("존재하지 않는 주문 테이블 ID로 주문을 등록한다")
    @Test
    void testCreateOrderWithNonExistentOrderTable() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(DefaultData.메뉴_후라이드치킨_ID);

        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        order.setOrderTableId(0L);

        List<Long> menuIds = Collections.singletonList(DefaultData.메뉴_후라이드치킨_ID);

        given(menuDao.countByIdIn(menuIds)).willReturn(1L);
        given(orderTableDao.findById(0L)).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("빈 주문 테이블로 주문 등록한다")
    @Test
    void testCreateOrderWithEmptyOrderTable() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(DefaultData.메뉴_후라이드치킨_ID);

        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        order.setOrderTableId(DefaultData.주문테이블_1번_ID);

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        List<Long> menuIds = Collections.singletonList(DefaultData.메뉴_후라이드치킨_ID);

        given(menuDao.countByIdIn(menuIds)).willReturn(1L);
        given(orderTableDao.findById(DefaultData.주문테이블_1번_ID)).willReturn(Optional.of(orderTable));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("존재하지 않는 주문의 상태를 업데이트한다")
    @Test
    void testChangeOrderStatusWithNonExistent() {
        // given
        Order order = new Order();
        given(orderDao.findById(0L)).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(0L, order));
    }

    @DisplayName("주문 상태가 계산 완료인 주문의 상태를 업데이트한다")
    @Test
    void testChangeOrderStatusWithCompletionOrder() {
        // given
        Order order = new Order();
        Order savedOrder = new Order();
        savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(1L)).willReturn(Optional.of(savedOrder));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(1L, order));
    }
}
