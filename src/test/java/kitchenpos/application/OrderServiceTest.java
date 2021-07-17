package kitchenpos.application;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@SpringBootTest
class OrderServiceTest {
    @MockBean
    private MenuDao menuDao;
    @MockBean
    private OrderDao orderDao;
    @MockBean
    private OrderLineItemDao orderLineItemDao;
    @MockBean
    private OrderTableDao orderTableDao;

    private OrderService orderService;
    private Order 주문;
    private OrderTable 테이블_1번;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

        테이블_1번 = new OrderTable();
        테이블_1번.setId(1L);
        테이블_1번.setEmpty(false);
        테이블_1번.setNumberOfGuests(3);

        Menu 면종류 = new Menu();
        면종류.setId(1L);
        면종류.setName("면종류");
        면종류.setPrice(new BigDecimal(10000L));

        OrderLineItem 주문항목1 = new OrderLineItem();
        주문항목1.setSeq(1L);
        주문항목1.setQuantity(5L);
        주문항목1.setMenuId(면종류.getId());

        주문 = new Order();
        주문.setId(1L);
        주문.setOrderTableId(테이블_1번.getId());
        주문.setOrderStatus(OrderStatus.COOKING.name());
        주문.setOrderLineItems(singletonList(주문항목1));
        주문항목1.setOrderId(주문.getId());
    }

    @DisplayName("주문 생성")
    @Test
    void create() {
        when(menuDao.countByIdIn(주문.getOrderLineItems().stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList()))
        ).thenReturn((long)주문.getOrderLineItems().size());
        when(orderTableDao.findById(주문.getOrderTableId())).thenReturn(Optional.of(테이블_1번));
        when(orderDao.save(주문)).thenReturn(주문);
        when(orderLineItemDao.save(any())).thenReturn(any());

        assertThat(orderService.create(주문)).isEqualTo(주문);
    }

    @DisplayName("포함되지 않은 메뉴로 인한 주문 생성 실패")
    @Test
    void createFailCauseNotIncludeMenu() {
        when(menuDao.countByIdIn(주문.getOrderLineItems().stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList()))
        ).thenReturn(0L);

        assertThatThrownBy(() -> orderService.create(주문)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블로 인한 주문 생성 실패")
    @Test
    void createFailCauseEmptyTable() {
        테이블_1번.setEmpty(true);
        when(menuDao.countByIdIn(주문.getOrderLineItems().stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList())))
            .thenReturn((long)주문.getOrderLineItems().size());
        when(orderTableDao.findById(주문.getOrderTableId())).thenReturn(Optional.of(테이블_1번));

        assertThatThrownBy(() -> orderService.create(주문)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 주문 조회")
    @Test
    void list() {
        when(orderDao.findAll()).thenReturn(singletonList(주문));
        assertThat(orderService.list()).contains(주문);
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        when(orderDao.findById(주문.getId())).thenReturn(Optional.of(주문));
        when(orderDao.save(주문)).thenReturn(주문);
        when(orderLineItemDao.findAllByOrderId(주문.getId())).thenReturn(주문.getOrderLineItems());

        assertThat(orderService.changeOrderStatus(주문.getId(), 주문)).isEqualTo(주문);
    }

    @DisplayName("완료된 주문으로 인한 상태 변경 실패")
    @Test
    void changeOrderStatusFailCauseComplete() {
        주문.setOrderStatus(OrderStatus.COMPLETION.name());
        when(orderDao.findById(주문.getId())).thenReturn(Optional.of(주문));

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문)).isInstanceOf(
            IllegalArgumentException.class);
    }
}