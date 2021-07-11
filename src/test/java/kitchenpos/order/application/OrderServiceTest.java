package kitchenpos.order.application;

import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenposNew.menu.domain.MenuRepository;
import kitchenposNew.order.OrderStatus;
import kitchenposNew.order.application.OrderService;
import kitchenposNew.order.domain.Order;
import kitchenposNew.order.domain.OrderRepository;
import kitchenposNew.order.dto.OrderRequest;
import kitchenposNew.order.dto.OrderResponse;
import kitchenposNew.order.exception.EmptyOrderLineItemsException;
import kitchenposNew.order.exception.NotEqualsOrderCountAndMenuCount;
import kitchenposNew.order.exception.NotFoundOrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
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
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문항목이 없으면 예외발생")
    void isBlankOrderLineItem_exception() {
        // given
        // 주문 생성되어 있음
        OrderRequest orderRequest = new OrderRequest(1L, new ArrayList<>());

        // then
        // 주문 요청 시 오류 발생
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(EmptyOrderLineItemsException.class);
    }

    @Test
    @DisplayName("주문의 메뉴 수가 실제 메뉴 수와 맞지 않으면 예외발생")
    void isNotCollectQuantityMenuCount_exception() {
        // given
        // 주문 아이템 생성됨
        firstOrderLineItem = 주문_아이템_생성(1L);
        secondOrderLineItem = 주문_아이템_생성(2L);

        // and
        // 주문 생성되어 있음
        OrderRequest order = new OrderRequest(1L, Arrays.asList(firstOrderLineItem, secondOrderLineItem));

        // when
        // 실제로 한개의 메뉴만 있음
        when(menuRepository.countByIdIn(Arrays.asList(1L, 2L))).thenReturn(1L);

        // then
        // 주문 생성 요청하면 오류 발생
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(NotEqualsOrderCountAndMenuCount.class);
    }

    @Test
    @DisplayName("주문 테이블 번호가 없는 예외")
    void isNotExistOrderTable() {
        // given
        // 주문 아이템 생성됨
        firstOrderLineItem = 주문_아이템_생성(1L);
        secondOrderLineItem = 주문_아이템_생성(2L);
        when(menuRepository.countByIdIn(Arrays.asList(1L, 2L))).thenReturn(2L);

        // and
        // 주문 생성되어 있음
        OrderRequest order = new OrderRequest(1L, Arrays.asList(firstOrderLineItem, secondOrderLineItem));

        // when
        // 실제로 한개의 메뉴만 있음
        when(orderTableDao.findById(1L)).thenReturn(Optional.empty());

        // then
        // 주문 요청함
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(NotFoundOrderTable.class);
    }

    @Test
    @DisplayName("주문 정상 접수 됨")
    void 주문_정상_접수() {
        // given
        // 주문 아이템 생성됨
        firstOrderLineItem = 주문_아이템_생성(1L);
        secondOrderLineItem = 주문_아이템_생성(2L);
        when(menuRepository.countByIdIn(Arrays.asList(1L, 2L))).thenReturn(2L);

        // and
        // 주문 생성되어 있음
        OrderRequest orderRequest = new OrderRequest(1L, Arrays.asList(firstOrderLineItem, secondOrderLineItem));

        // and
        // 주문 테이블 생성되어 있음
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        Order order = new Order(orderTable, Arrays.asList(firstOrderLineItem, secondOrderLineItem));

        when(orderTableDao.findById(1L)).thenReturn(Optional.ofNullable(orderTable));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderLineItemDao.save(firstOrderLineItem)).thenReturn(firstOrderLineItem);
        when(orderLineItemDao.save(secondOrderLineItem)).thenReturn(secondOrderLineItem);

        // when
        // 주문 요청함
        OrderResponse expected = orderService.create(orderRequest);

        // than
        // 주문 접수됨
        assertThat(expected.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    private OrderLineItem 주문_아이템_생성(Long menuId) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        return orderLineItem;
    }
}
