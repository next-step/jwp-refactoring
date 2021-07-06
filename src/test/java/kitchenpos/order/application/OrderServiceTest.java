package kitchenpos.order.application;

import static kitchenpos.table.application.TableServiceTest.두명;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 서비스")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private static final boolean 비어있음 = true;
    private static final boolean 비어있지않음 = false;
    private static final Long 주문테이블_ID = 1L;
    private static final Long 주문_ID = 1L;
    private static final OrderTable 주문테이블 = new OrderTable(주문테이블_ID, 두명, 비어있지않음);
    private static final OrderLineItem 첫번째_주문항목 = new OrderLineItem(주문_ID, 1L, 1);
    private static final OrderLineItem 두번째_주문항목 = new OrderLineItem(주문_ID, 2L, 1);
    private static final List<OrderLineItem> 주문_항목_목록 = new ArrayList<>(Arrays.asList(첫번째_주문항목, 두번째_주문항목));

    @Mock
    private OrderDao orderDao;
    @Mock
    private MenuDao menuDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        // Given
        OrderRequest 주문 = new OrderRequest(주문테이블_ID, OrderStatus.COOKING, LocalDateTime.now(), OrderLineItemRequest.listOf(주문_항목_목록));
        given(menuDao.countByIdIn(any())).willReturn(2L);
        given(orderTableDao.findById(any())).willReturn(Optional.of(주문테이블));
        given(orderDao.save(any())).willReturn(주문.toOrder());

        // When
        orderService.create(주문);

        // Then
        verify(menuDao, times(1)).countByIdIn(any());
        verify(orderTableDao, times(1)).findById(any());
        verify(orderDao, times(1)).save(any());
    }

    @DisplayName("주문 항목은 1개 이상 이어야한다.")
    @Test
    void create_Fail_01() {
        // Given
        List<OrderLineItemRequest> 비어있는_주문_항목_목록 = new ArrayList<>();
        OrderRequest 주문 = new OrderRequest(주문테이블_ID, OrderStatus.COOKING, LocalDateTime.now(), 비어있는_주문_항목_목록);

        // When & Then
        assertThatThrownBy(() -> orderService.create(주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목의 메뉴는 모두 존재해야 한다.")
    @Test
    void create_Fail_02() {
        // Given
        OrderRequest 주문 = new OrderRequest(주문테이블_ID, OrderStatus.COOKING, LocalDateTime.now(), OrderLineItemRequest.listOf(주문_항목_목록));

        // When & Then
        assertThatThrownBy(() -> orderService.create(주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블은 빈 테이블일 수 없다.")
    @Test
    void create_Fail_03() {
        // Given
        OrderTable 비어있는_주문테이블 = new OrderTable(주문테이블_ID, 두명, 비어있음);
        OrderRequest 주문 = new OrderRequest(비어있는_주문테이블.getId(), OrderStatus.COOKING, LocalDateTime.now(), OrderLineItemRequest.listOf(주문_항목_목록));
        List<Menu> menus = new ArrayList<>(Arrays.asList(new Menu(1L), new Menu(2L)));
        given(menuDao.countByIdIn(any())).willReturn(2L);
        given(orderTableDao.findById(any())).willReturn(Optional.of(비어있는_주문테이블));

        // When & Then
        assertThatThrownBy(() -> orderService.create(주문))
            .isInstanceOf(IllegalArgumentException.class);

        verify(menuDao, times(1)).countByIdIn(any());
        verify(orderTableDao, times(1)).findById(any());
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        // Given
        OrderRequest 주문 = new OrderRequest(주문테이블_ID, OrderStatus.COOKING, LocalDateTime.now(), OrderLineItemRequest.listOf(주문_항목_목록));
        List<Order> 주문_목록 = new ArrayList<>(Arrays.asList(주문.toOrder()));
        given(orderDao.findAll()).willReturn(주문_목록);

        // When & Then
        assertThat(orderService.list()).hasSize(1);
        verify(orderDao, times(1)).findAll();
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // Given
        OrderStatus 진행상태 = OrderStatus.COOKING;
        OrderRequest 주문 = new OrderRequest(주문_ID, 주문테이블_ID, 진행상태, LocalDateTime.now(), OrderLineItemRequest.listOf(주문_항목_목록));
        given(orderDao.findById(any())).willReturn(Optional.of(주문.toOrder()));

        // When
        orderService.changeOrderStatus(주문_ID, 진행상태);

        // Then
        verify(orderDao, times(1)).findById(any());
    }

    @DisplayName("주문상태가 계산완료인 주문은 변경할 수 없다.")
    @Test
    void changeOrderStatus_Fail() {
        // Given
        OrderStatus 진행상태 = OrderStatus.COMPLETION;
        OrderRequest 주문 = new OrderRequest(주문_ID, 주문테이블_ID, 진행상태, LocalDateTime.now(), OrderLineItemRequest.listOf(주문_항목_목록));
        given(orderDao.findById(any())).willReturn(Optional.of(주문.toOrder()));

        // When & Then
        assertThatThrownBy(() -> orderService.changeOrderStatus(주문_ID, 진행상태))
            .isInstanceOf(IllegalArgumentException.class);
    }

}
