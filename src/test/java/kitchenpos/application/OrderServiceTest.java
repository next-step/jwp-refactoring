package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuDao menuDao;
    @Mock
    private TableService tableService;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderDao orderDao;
    @InjectMocks
    private OrderService orderService;
    private Menu 메뉴;
    private Order 주문;
    private Order 주문_변경_요청;
    private OrderLineItem 주문_항목;
    private OrderTable 좌석;

    @BeforeEach
    void setUp() {
        메뉴 = new Menu("후라이드치킨", BigDecimal.valueOf(16000), null, null);
        주문_항목 = new OrderLineItem(1L, null, 메뉴.getId(), 1L);
        좌석 = new OrderTable(1L, null, 1, false);
        주문 = new Order(좌석.getId(), null, LocalDateTime.now(), Arrays.asList(주문_항목));
        주문_변경_요청 = new Order(좌석.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(), Arrays.asList(주문_항목));
    }

    @Test
    void 생성() {
        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(tableService.findById(anyLong())).willReturn(좌석);
        given(orderDao.save(any())).willReturn(주문);
        given(orderLineItemDao.save(any())).willReturn(주문_항목);

        OrderResponse response = orderService.create(주문);

        assertThat(response.getOrderTableId()).isEqualTo(좌석.getId());
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(response.getOrderLineItems()).containsExactly(주문_항목);
    }

    @Test
    void 주문_항목이_empty인_경우() {
        주문 = new Order(좌석.getId(), null, LocalDateTime.now(), Collections.emptyList());

        assertThatThrownBy(
                () -> orderService.create(주문)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_항목의_수와_등록된_메뉴의_수가_같지_않은_경우() {
        given(menuDao.countByIdIn(anyList())).willReturn(0L);

        assertThatThrownBy(
                () -> orderService.create(주문)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 좌석이_공석인_경우() {
        좌석 = new OrderTable(1L, null, 1, true);
        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(tableService.findById(anyLong())).willReturn(좌석);

        assertThatThrownBy(
                () -> orderService.create(주문)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 조회() {
        given(orderDao.findAll()).willReturn(Arrays.asList(주문));
        given(orderLineItemDao.findAllByOrderId(주문.getId())).willReturn(Arrays.asList(주문_항목));

        List<OrderResponse> orders = orderService.list();

        assertAll(
                () -> assertThat(orders.size()).isEqualTo(1),
                () -> assertThat(orders.get(0).getOrderLineItems()).containsExactly(주문_항목)
        );
    }

    @Test
    void 주문_상태_변경() {
        given(orderDao.findById(anyLong())).willReturn(Optional.of(주문));
        given(orderLineItemDao.findAllByOrderId(anyLong())).willReturn(Arrays.asList(주문_항목));

        OrderResponse response = orderService.changeOrderStatus(anyLong(), 주문_변경_요청);

        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
        assertThat(response.getOrderLineItems()).containsExactly(주문_항목);
    }

    @Test
    void 주문이_등록되어_있지_않은_경우() {
        given(orderDao.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(
                () -> orderService.changeOrderStatus(anyLong(), 주문_변경_요청)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_상태가_완료인_경우() {
        given(orderDao.findById(anyLong())).willReturn(Optional.of(주문_변경_요청));

        assertThatThrownBy(
                () -> orderService.changeOrderStatus(anyLong(), 주문_변경_요청)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
