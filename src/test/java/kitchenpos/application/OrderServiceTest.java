package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.domain.type.OrderStatus;
import kitchenpos.port.MenuPort;
import kitchenpos.port.OrderLineItemPort;
import kitchenpos.port.OrderPort;
import kitchenpos.port.OrderTablePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {


    @Mock
    private MenuPort menuDao;
    @Mock
    private OrderPort orderPort;
    @Mock
    private OrderLineItemPort orderLineItemPort;
    @Mock
    private OrderTablePort orderTablePort;
    @InjectMocks
    private OrderService orderService;

    private Product 후라이드치킨;
    private Product 제로콜라;
    private MenuGroup 치킨;
    private MenuProduct 후라이드_이인분;
    private MenuProduct 제로콜라_삼인분;
    private Menu 후치콜세트;
    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        후라이드치킨 = new Product(1L, "후라이드치킨", BigDecimal.valueOf(3_000));
        제로콜라 = new Product(2L, "제로콜라", BigDecimal.valueOf(2_000));

        치킨 = new MenuGroup(1L, "치킨");

        후라이드_이인분 = new MenuProduct(1L, 1L, 후라이드치킨.getId(), 2);
        제로콜라_삼인분 = new MenuProduct(1L, 1L, 제로콜라.getId(), 3);

        후치콜세트 = new Menu(1L, "후치콜세트", BigDecimal.valueOf(5_000), 치킨.getId(), Arrays.asList(제로콜라_삼인분, 후라이드_이인분));

        주문테이블 = new OrderTable(1L, null, 0, false);
    }

    @Test
    @DisplayName("주문을 등록 할 수 있다")
    void createOrder() {
        Order 주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>());
        OrderLineItem 주문_항목 = new OrderLineItem(1L, 주문.getId(), 후치콜세트.getId(), 1);
        주문.setOrderLineItems(Arrays.asList(주문_항목));

        when(menuDao.countByIdIn(any())).thenReturn(1L);
        when(orderTablePort.findById(any())).thenReturn(Optional.of(주문테이블));
        when(orderPort.save(any())).thenReturn(주문);
        when(orderLineItemPort.save(any())).thenReturn(주문_항목);

        Order result = orderService.create(주문);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getOrderStatus()).isNotNull();
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(result.getOrderLineItems()).contains(주문_항목);
    }

    @Test
    @DisplayName("주문 항목이 비어 있을 수 없다.")
    void createOrderEmpty() {
        Order 주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>());

        assertThatThrownBy(() ->
                orderService.create(주문)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 항목의 수와 등록된 메뉴의 수는 같아야한다.")
    void createOrderSameMenuSize() {
        Order 주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>());
        OrderLineItem 주문_항목 = new OrderLineItem(1L, 주문.getId(), 후치콜세트.getId(), 1);
        주문.setOrderLineItems(Arrays.asList(주문_항목));

        when(menuDao.countByIdIn(any())).thenReturn(0L);

        assertThatThrownBy(() ->
                orderService.create(주문)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블에 등록 되어 있어야한다.")
    void createOrderAlreadyMenu() {
        Order 주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>());
        OrderLineItem 주문_항목 = new OrderLineItem(1L, 주문.getId(), 후치콜세트.getId(), 1);
        주문.setOrderLineItems(Arrays.asList(주문_항목));

        when(menuDao.countByIdIn(any())).thenReturn(1L);
        when(orderTablePort.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                orderService.create(주문)
        ).isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("주문 리스트를 받을 수 있다.")
    void getOrderList() {
        Order 주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>());
        OrderLineItem 주문_항목 = new OrderLineItem(1L, 주문.getId(), 후치콜세트.getId(), 1);
        주문.setOrderLineItems(Arrays.asList(주문_항목));

        when(orderPort.findAll()).thenReturn(Collections.singletonList(주문));
        when(orderLineItemPort.findAllByOrderId(주문.getId())).thenReturn(Arrays.asList(주문_항목));

        List<Order> result = orderService.list();

        assertThat(result).hasSize(1);
        assertThat(result.stream().map(Order::getId)).contains(주문.getId());
    }

    @Test
    @DisplayName("주문 상태를 변경 할 수 있다.")
    void changeOrderStatus() {
        Order 주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>());
        Order 주문변경 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>());

        when(orderPort.findById(주문.getId())).thenReturn(Optional.of(주문));

        orderService.changeOrderStatus(주문.getId(), 주문변경);

        assertThat(주문.getOrderStatus()).isEqualTo(주문변경.getOrderStatus());
    }

    @Test
    @DisplayName("등록된 주문이 있어야한다.")
    void changeOrderStatusAlreadyMenu() {
        Order 주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>());
        Order 새로운_주문변경 = new Order(2L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>());
        when(orderPort.findById(주문.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                orderService.changeOrderStatus(주문.getId(), 새로운_주문변경)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("계산이 완료된 상태이면 주문 상태 변경이 불가능하다.")
    void changeOrderStatusNotCompleteChange() {
        Order 주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>());
        주문.setOrderStatus(OrderStatus.COMPLETION.name());
        Order 주문변경 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>());

        when(orderPort.findById(주문.getId())).thenReturn(Optional.of(주문));

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문변경))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
