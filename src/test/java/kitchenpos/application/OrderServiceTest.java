package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    MenuDao menuDao;
    @Mock
    OrderDao orderDao;
    @Mock
    OrderLineItemDao orderLineItemDao;
    @Mock
    OrderTableDao orderTableDao;
    @InjectMocks
    OrderService orderService;

    Menu 메뉴_후라이드_후라이드;
    OrderLineItem 주문라인아이템;
    OrderTable 주문테이블;
    Order 주문;
    Order 주문_변경;

    @BeforeEach
    void setUp() {
        메뉴_후라이드_후라이드 = new Menu();
        메뉴_후라이드_후라이드.setId(1L);

        주문라인아이템 = new OrderLineItem();
        주문라인아이템.setMenuId(메뉴_후라이드_후라이드.getId());

        주문테이블 = new OrderTable();
        주문테이블.setId(1L);

        주문 = new Order();
        주문.setId(1L);
        주문.setOrderTableId(주문테이블.getId());
        주문.setOrderLineItems(Arrays.asList(주문라인아이템));
        주문.setOrderStatus(OrderStatus.MEAL.name());

        주문_변경 = new Order();
        주문_변경.setOrderStatus(OrderStatus.COMPLETION.name());
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void create() {
        //given
        when(menuDao.countByIdIn(Arrays.asList(메뉴_후라이드_후라이드.getId()))).thenReturn(Long.valueOf(주문.getOrderLineItems().size()));
        when(orderTableDao.findById(주문테이블.getId())).thenReturn(Optional.of(주문테이블));
        when(orderDao.save(주문)).thenReturn(주문);
        when(orderLineItemDao.save(주문라인아이템)).thenReturn(주문라인아이템);

        //when
        Order createdOrder = orderService.create(주문);

        //then
        assertThat(createdOrder.getId()).isEqualTo(주문.getId());
    }

    @Test
    @DisplayName("주문항목이 비어있을 경우 주문 생성을 실패한다.")
    void create_with_exception_when_order_line_items_is_empty() {
        //given
        주문.setOrderLineItems(Arrays.asList());

        //when
        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    //TODO
    @Test
    @DisplayName("저장된 메뉴에 없는 메뉴일 경우 주문 생성을 실패한다.")
    void create_with_exception_when_menu_not_in_saved_menus() {
        //given
        when(menuDao.countByIdIn(Arrays.asList(메뉴_후라이드_후라이드.getId()))).thenReturn(0L);

        //when && then
        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블에서 주문할 경우 주문 생성을 실패한다.")
    void create_with_exception_when_table_is_null() {
        //given
        주문테이블.setEmpty(true);

        when(menuDao.countByIdIn(Arrays.asList(메뉴_후라이드_후라이드.getId()))).thenReturn(Long.valueOf(주문.getOrderLineItems().size()));
        when(orderTableDao.findById(주문테이블.getId())).thenReturn(Optional.of(주문테이블));

        //when && then
        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("전체 주문을 조회한다.")
    void list() {
        //given
        when(orderDao.findAll()).thenReturn(Arrays.asList(주문));

        //when
        List<Order> foundOrders = orderService.list();

        //then
        assertThat(foundOrders).containsExactly(주문);
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        //given
        when(orderDao.findById(주문.getId())).thenReturn(Optional.of(주문));
        when(orderLineItemDao.findAllByOrderId(주문.getId())).thenReturn(주문.getOrderLineItems());

        //when
        Order changedOrder = orderService.changeOrderStatus(주문.getId(), 주문_변경);

        //then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(주문_변경.getOrderStatus());
    }

    @Test
    @DisplayName("주문번호가 없는 경우 주문 상태 변경을 실패한다.")
    void changeOrderStatus_with_exception_when_order_is_null() {
        //given
        when(orderDao.findById(any())).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문_변경))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문상태가 계산완료인경우 주문 상태 변경을 실패한다.")
    void changeOrderStatus_with_exception_when_order_status_is_completion() {
        //given
        주문.setOrderStatus(OrderStatus.COMPLETION.name());

        when(orderDao.findById(주문.getId())).thenReturn(Optional.of(주문));

        //when
        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문_변경))
                .isInstanceOf(IllegalArgumentException.class);
    }
}