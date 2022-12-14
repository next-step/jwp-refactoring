package kitchenpos.order.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문생성 테스트")
    @Test
    void createOrderTest() {
        //given
        OrderLineItem 주문항목1 = new OrderLineItem(1L, 1L, 1L, 1);
        OrderLineItem 주문항목2 = new OrderLineItem(2L, 1L, 2L, 1);
        OrderTable 주문테이블 = new OrderTable(1L, null, new NumberOfGuests(4), false);
        Order 주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(),
                Arrays.asList(주문항목1, 주문항목2));

        when(menuRepository.findAllById(주문.getOrderLineItems().stream().map(OrderLineItem::getMenuId).collect(Collectors.toList())).size())
                .thenReturn(주문.getOrderLineItems().size());
        when(orderTableRepository.findById(주문.getOrderTableId())).thenReturn(Optional.ofNullable(주문테이블));
        when(orderDao.save(주문)).thenReturn(주문);
        for(final OrderLineItem orderLineItem : 주문.getOrderLineItems()) {
            when(orderLineItemDao.save(orderLineItem)).thenReturn(orderLineItem);
        }

        //when
        Order result = orderService.create(주문);

        //then
        assertThat(result).isEqualTo(주문);
    }


    @DisplayName("빈 주문항목 목록으로 주문생성 오류 테스트")
    @Test
    void createOrderEmptyOrderLinesExceptionTest() {
        //given
        Order 주문 = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                Arrays.asList());

        //when
        //then
        assertThatThrownBy(() -> orderService.create(주문)).isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("주문항목에 있는 메뉴가 존재하지 않는 경우 주문생성 오류 테스트")
    @Test
    void createOrderWithNotExistMenuExceptionTest() {
        //given
        OrderLineItem 주문항목1 = new OrderLineItem(1L, 1L, 1L, 1);
        OrderLineItem 주문항목2 = new OrderLineItem(2L, 1L, 2L, 1);
        Order 주문 = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                Arrays.asList(주문항목1, 주문항목2));

        when(menuRepository.findAllById(주문.getOrderLineItems().stream().map(OrderLineItem::getMenuId).collect(Collectors.toList())).size())
                .thenReturn(0);

        //when
        //then
        assertThatThrownBy(() -> orderService.create(주문)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 주문테이블로 주문생성 오류 테스트")
    @Test
    void createOrderWithNotExistOrderTableExceptionTest() {
        //given
        OrderLineItem 주문항목1 = new OrderLineItem(1L, 1L, 1L, 1);
        OrderLineItem 주문항목2 = new OrderLineItem(2L, 1L, 2L, 1);
        Order 주문 = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                Arrays.asList(주문항목1, 주문항목2));

        when(menuRepository.findAllById(주문.getOrderLineItems().stream().map(OrderLineItem::getMenuId).collect(Collectors.toList())).size())
                .thenReturn(주문.getOrderLineItems().size());
        when(orderTableRepository.findById(주문.getOrderTableId())).thenReturn(Optional.ofNullable(null));

        //when
        //then
        assertThatThrownBy(() -> orderService.create(주문)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 주문테이블로 주문생성 오류 테스트")
    @Test
    void createOrderWithEmptyOrderTableExceptionTest() {
        //given
        OrderLineItem 주문항목1 = new OrderLineItem(1L, 1L, 1L, 1);
        OrderLineItem 주문항목2 = new OrderLineItem(2L, 1L, 2L, 1);
        OrderTable 주문테이블 = new OrderTable(1L, null, new NumberOfGuests(0), true);
        Order 주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(),
                Arrays.asList(주문항목1, 주문항목2));

        when(menuRepository.findAllById(주문.getOrderLineItems().stream().map(OrderLineItem::getMenuId).collect(Collectors.toList())).size())
                .thenReturn(주문.getOrderLineItems().size());
        when(orderTableRepository.findById(주문.getOrderTableId())).thenReturn(Optional.ofNullable(주문테이블));

        //when
        //then
        assertThatThrownBy(() -> orderService.create(주문)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문목록 조회 테스트")
    @Test
    void retrieveOrdersTest() {
        //given
        OrderLineItem 주문항목1 = new OrderLineItem(1L, 1L, 1L, 1);
        OrderLineItem 주문항목2 = new OrderLineItem(2L, 1L, 2L, 1);
        OrderLineItem 주문항목3 = new OrderLineItem(3L, 2L, 1L, 1);
        Order 주문1 = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                Arrays.asList(주문항목1, 주문항목2));
        Order 주문2 = new Order(2L, 2L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                Arrays.asList(주문항목3));
        when(orderDao.findAll()).thenReturn(Arrays.asList(주문1, 주문2));
        for(final Order order : Arrays.asList(주문1, 주문2)) {
            when(orderLineItemDao.findAllByOrderId(order.getId())).thenReturn(order.getOrderLineItems());
        }

        //when
        List<Order> orders = orderService.list();

        //then
        assertThat(orders).contains(주문1, 주문2);
    }

    @DisplayName("주문상태변경 테스트")
    @Test
    void changeOrderStatusTest() {
        //given
        OrderLineItem 주문항목1 = new OrderLineItem(1L, 1L, 1L, 1);
        OrderLineItem 주문항목2 = new OrderLineItem(2L, 1L, 2L, 1);
        OrderTable 주문테이블 = new OrderTable(1L, null, new NumberOfGuests(4), false);
        Order 주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(),
                Arrays.asList(주문항목1, 주문항목2));

        when(orderDao.findById(주문.getId())).thenReturn(Optional.ofNullable(주문));
        when(orderDao.save(주문)).thenReturn(주문);
        when(orderLineItemDao.findAllByOrderId(주문.getId())).thenReturn(주문.getOrderLineItems());

        //when
        Order result = orderService.changeOrderStatus(주문.getId(), 주문);

        //then
        assertThat(result).isEqualTo(주문);
    }

    @DisplayName("존재하지 않는 주문의 상태변경 테스트")
    @Test
    void changeOrderStatusWithNotExistIdExceptionTest() {
        //given
        OrderLineItem 주문항목1 = new OrderLineItem(1L, 1L, 1L, 1);
        OrderLineItem 주문항목2 = new OrderLineItem(2L, 1L, 2L, 1);
        OrderTable 주문테이블 = new OrderTable(1L, null, new NumberOfGuests(4), false);
        Order 주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(),
                Arrays.asList(주문항목1, 주문항목2));

        when(orderDao.findById(주문.getId())).thenReturn(Optional.ofNullable(null));

        //when
        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("완료상태인 주문의 상태변경 오류 테스트")
    @Test
    void changeCompleteOrderStatusExceptionTest() {
        //given
        OrderLineItem 주문항목1 = new OrderLineItem(1L, 1L, 1L, 1);
        OrderLineItem 주문항목2 = new OrderLineItem(2L, 1L, 2L, 1);
        OrderTable 주문테이블 = new OrderTable(1L, null, new NumberOfGuests(4), false);
        Order 주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(),
                Arrays.asList(주문항목1, 주문항목2));

        when(orderDao.findById(주문.getId())).thenReturn(Optional.ofNullable(주문));

        //when
        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
