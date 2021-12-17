package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

/**
 * packageName : kitchenpos.application
 * fileName : MenuServiceTest
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
@DisplayName("주문 비즈니스 오브젝트 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    Order order;

    @Mock(name = "firstOrderLineItem")
    OrderLineItem firstOrderLineItem;

    @Mock(name = "secondOrderLineItem")
    OrderLineItem secondOrderLineItem;

    @Mock
    OrderTable orderTable;

    @Mock
    OrderDao orderDao;

    @Mock
    MenuDao menuDao;

    @Mock
    OrderLineItemDao orderLineItemDao;

    @Mock
    OrderTableDao orderTableDao;

    @InjectMocks
    OrderService orderService;

    @Test
    @DisplayName("apsb 생성한다.")
    public void create() throws Exception {
        // given
        given(order.getOrderLineItems()).willReturn(Lists.newArrayList(firstOrderLineItem, secondOrderLineItem));
        given(firstOrderLineItem.getMenuId()).willReturn(1L);
        given(secondOrderLineItem.getMenuId()).willReturn(2L);
        given(menuDao.countByIdIn(anyList())).willReturn(Long.valueOf(2));
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderTable.isEmpty()).willReturn(false);
        given(orderTable.getId()).willReturn(1L);
        given(orderDao.save(any(Order.class))).willReturn(order);
        given(order.getId()).willReturn(1L);
        given(orderLineItemDao.save(any(OrderLineItem.class))).willReturn(firstOrderLineItem, secondOrderLineItem);
        // when

        orderService.create(order);
        // then

    }
}