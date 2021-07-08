package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("주문 기능 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    private OrderLineItem firstOrderLineItem;
    private OrderLineItem secondOrderLineItem;

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

    @Test
    @DisplayName("주문항목이 없으면 예외발생")
    void isBlankOrderLineItem_exption() {
        // given
        // 주문 생성되어 있음
        Order order = new Order();

        // then
        // 주문 요청 시 오류 발생
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문의 메뉴 수가 실제 메뉴 수와 맞지 않으면 예외발생")
    void isNotCollectQuantityMenuCount_exception() {
        // given
        // 주문 아이템 생성됨
        주문_아이템_생성됨();

        // and
        // 주문 생성되어 있음
        Order order = new Order();
        order.setOrderLineItems(Arrays.asList(firstOrderLineItem, secondOrderLineItem));

        // when
        // 실제로 한개의 메뉴만 있음
        when(menuDao.countByIdIn(any())).thenReturn(1L);

        // then
        // 주문 생성 요청하면 오류 발생
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 번호가 없는 예외")
    void isNotExistOrderTable() {
        // given
        // 주문 아이템 생성됨
        주문_아이템_생성됨();
        when(menuDao.countByIdIn(any())).thenReturn(2L);

        // and
        // 주문 생성되어 있음
        Order order = new Order();
        order.setOrderLineItems(Arrays.asList(firstOrderLineItem, secondOrderLineItem));

        // when
        // 실제로 한개의 메뉴만 있음
        when(orderTableDao.findById(any())).thenReturn(Optional.empty());

        // then
        // 주문 요청함
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 정상 접수 됨")
    void 주문_정상_접수() {
        // given
        // 주문 아이템 생성됨
        주문_아이템_생성됨();
        when(menuDao.countByIdIn(any())).thenReturn(2L);

        // and
        // 주문 생성되어 있음
        Order order = new Order();
        order.setId(1L);
        order.setOrderLineItems(Arrays.asList(firstOrderLineItem, secondOrderLineItem));

        // and
        // 주문 테이블 생성되어 있음
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        when(orderTableDao.findById(any())).thenReturn(Optional.ofNullable(orderTable));
        when(orderDao.save(any())).thenReturn(order);
        when(orderLineItemDao.save(firstOrderLineItem)).thenReturn(firstOrderLineItem);
        when(orderLineItemDao.save(secondOrderLineItem)).thenReturn(secondOrderLineItem);

        // when
        // 주문 요청함
        Order expected = orderService.create(order);

        // than
        // 주문 접수됨
        assertThat(expected.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    private void 주문_아이템_생성됨() {
        firstOrderLineItem = new OrderLineItem();
        firstOrderLineItem.setMenuId(1L);
        secondOrderLineItem = new OrderLineItem();
        secondOrderLineItem.setMenuId(2l);
    }


}
