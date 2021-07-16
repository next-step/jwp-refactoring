package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.exception.AlreadyCompletionException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    MenuRepository menuRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    private OrderTable 주문테이블;
    private OrderTable new주문테이블;
    private OrderTables 주문테이블묶음;
    private TableGroup 테이블그룹;
    private OrderLineItem 주문수량메뉴1;
    private OrderLineItem 주문수량메뉴2;
    private List<OrderLineItem> 주문내역들;
    private Order 주문;
    private OrderRequest 주문요청;
    private Order 다른주문;
    private Menu 치킨메뉴;
    private Menu 튀김메뉴;

    @BeforeEach
    void setUp() {
        치킨메뉴 = new Menu(1L, "치킨메뉴", BigDecimal.valueOf(20000));
        튀김메뉴 = new Menu(2L, "튀김메뉴", BigDecimal.valueOf(10000));
        주문수량메뉴1 = new OrderLineItem(1L, 치킨메뉴.getId(), 1L);
        주문수량메뉴2 = new OrderLineItem(2L, 튀김메뉴.getId(), 1L);
        주문내역들 = Arrays.asList(주문수량메뉴1, 주문수량메뉴2);
        주문테이블 = new OrderTable(1L, 1L, 4, false);
        new주문테이블 = new OrderTable(2L, 1L, 4, false);
        주문테이블묶음 = new OrderTables(Arrays.asList(주문테이블, new주문테이블));
        테이블그룹 = new TableGroup(1L);
        주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING, 주문내역들);
        주문요청 = new OrderRequest(주문테이블.getId(), 주문내역들);
        다른주문 = new Order(2L, OrderStatus.MEAL, 주문내역들);
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        given(menuRepository.findAllById(anyList())).willReturn(Arrays.asList(치킨메뉴, 튀김메뉴));
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문테이블));
        given(orderRepository.save(any())).willReturn(주문);

        OrderResponse savedOrder = orderService.create(주문요청);

        assertAll(
                () -> assertThat(savedOrder.getId()).isEqualTo(주문.getId()),
                () -> assertThat(savedOrder.getOrderLineItems())
                        .contains(OrderLineItemResponse.from(주문수량메뉴1), OrderLineItemResponse.from(주문수량메뉴2)));

        verify(menuRepository, times(1)).findAllById(anyList());
        verify(orderTableRepository, times(1)).findById(anyLong());
        verify(orderRepository, times(1)).save(any());
    }

    @DisplayName("주문을 등록을 실패한다 - 주문에 포함된 주문된 메뉴들이(주문 아이템[OrderLineItem]) 없으면 실패한다.")
    @Test
    void fail_create1() {
        OrderRequest order = new OrderRequest(1L, Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 등록을 실패한다 - 등록되지 않은 메뉴 주문시 등록 실패한다.")
    @Test
    void fail_create2() {
        OrderRequest order = new OrderRequest(1L, 주문내역들);
        given(menuRepository.findAllById(any())).willReturn(Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);

        verify(menuRepository, times(1)).findAllById(any());
    }

    @DisplayName("주문을 등록을 실패한다 - 주문 테이블이 없는 주문은 등록 실패한다.")
    @Test
    void fail_create3() {
        OrderRequest order = new OrderRequest(1L, 주문내역들);
        given(menuRepository.findAllById(any())).willReturn(Arrays.asList(치킨메뉴, 튀김메뉴));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);

        verify(menuRepository, times(1)).findAllById(any());
    }

    @DisplayName("주문을 등록을 실패한다 - 주문 테이블이 empty (true) 상태면 주문 등록 실패한다.")
    @Test
    void fail_create4() {
        OrderRequest order = new OrderRequest(1L, 주문내역들);
        given(menuRepository.findAllById(any())).willReturn(Arrays.asList(치킨메뉴, 튀김메뉴));
        given(orderTableRepository.findById(any())).willReturn(Optional.of(new OrderTable(1L, 테이블그룹.getId(), 2, true)));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);

        verify(menuRepository, times(1)).findAllById(any());
        verify(orderTableRepository, times(1)).findById(any());
    }

    @DisplayName("주문을 조회한다.")
    @Test
    void list() {
        given(orderRepository.findAll()).willReturn(Arrays.asList(주문, 다른주문));

        List<OrderResponse> orders = orderService.list();
        assertAll(
                () -> assertThat(orders.get(0).getOrderTableId()).isEqualTo(OrderResponse.from(주문).getOrderTableId()),
                () -> assertThat(orders.get(1).getOrderTableId()).isEqualTo(OrderResponse.from(다른주문).getOrderTableId()));

        verify(orderRepository, times(1)).findAll();
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        String changedStatus = OrderStatus.COMPLETION.name();
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(changedStatus);
        given(orderRepository.findById(anyLong())).willReturn(Optional.ofNullable(주문));

        OrderResponse changed = orderService.changeOrderStatus(주문.getId(), orderStatusRequest);

        assertAll(() -> assertThat(changed.getOrderStatus()).isEqualTo(changedStatus));

        verify(orderRepository, times(1)).findById(anyLong());
    }

    @DisplayName("주문 상태를 변경을 실패한다 - 기존에 등록된 주문이 없으면 주문 상태 변경에 실패한다.")
    @Test
    void fail_changeOrderStatus1() {
        String changedStatus = OrderStatus.COMPLETION.name();
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(changedStatus);
        given(orderRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), orderStatusRequest))
                .isInstanceOf(IllegalArgumentException.class);

        verify(orderRepository, times(1)).findById(anyLong());
    }

    @DisplayName("주문 상태를 변경을 실패한다 - 주문 상태가 기존에 계산 완료(COMPLETION) 상태일 경우 주문 상태 변경에 실패한다.")
    @Test
    void fail_changeOrderStatus2() {
        String changedStatus = OrderStatus.COMPLETION.name();
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(changedStatus);
        Order order = new Order(주문테이블.getId(), 1L, OrderStatus.COMPLETION, 주문내역들);
        given(orderRepository.findById(anyLong())).willReturn(Optional.ofNullable(order));

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), orderStatusRequest))
                .isInstanceOf(AlreadyCompletionException.class);

        verify(orderRepository, times(1)).findById(anyLong());
    }
}
