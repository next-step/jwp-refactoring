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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
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

    private TableGroup tableGroup;
    private OrderTable orderTable1;

    @BeforeEach
    void init() {
        tableGroup = new TableGroup(1L);
        orderTable1 = new OrderTable(1L, tableGroup.getId(), 3, false);
    }

    @DisplayName("주문 등록 테스트")
    @Test
    void 주문_등록() {
        //given
        Order order = new Order(1L, orderTable1.getId());
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 2L, 1);
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
        
        given(menuDao.countByIdIn(Arrays.asList(1L,2L))).willReturn(2L);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.ofNullable(orderTable1));
        given(orderDao.save(any())).willReturn(order);

        //when
        Order saved = orderService.create(order);

        //then
        assertAll(
                () -> assertThat(saved.getOrderTableId()).isEqualTo(orderTable1.getId()),
                () -> assertThat(saved.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(saved.getOrderLineItems().size()).isEqualTo(2)
        );
    }

    @DisplayName("주문항목의 메뉴가 중복될 경우 주문을 등록할 수 없다.")
    @Test
    void 주문항목의_메뉴가_중복될_경우_주문_등록_에러() {
        //given
        Order order = new Order(1L, orderTable1.getId());
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1L, 1);
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

        //when+then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(order));
    }

    @DisplayName("주문 테이블이 존재하지 않을 경우 주문을 등록 할 수 없다.")
    @Test
    void 주문_테이블이_존재하지_않을_경우_주문_등록_에러() {
        //given
        Order order = new Order(1L, null);
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 2L, 1);
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
        given(menuDao.countByIdIn(Arrays.asList(1L,2L))).willReturn(2L);

        //when+then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(order));
    }

    @DisplayName("주문은 하나 이상의 주문 항목을 가져야하며 주문 항목이 존재하지 않을 경우 주문을 등록할 수 없다.")
    @Test
    void 주문항목이_비어있을_경우_주문_등록_에러() {
        //given
        Order order = new Order(1L, orderTable1.getId());

        //when+then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(order));
    }

    @DisplayName("주문 목록 조회")
    @Test
    void 주문_목록_조회() {
        //given
        Order order = new Order(1L, orderTable1.getId());
        Order order2 = new Order(2L, orderTable1.getId());
        given(orderDao.findAll()).willReturn(Arrays.asList(order, order2));

        //when
        List<Order> orderList = orderService.list();

        assertThat(orderList.size()).isEqualTo(2);
    }

    @DisplayName("주문 상태 변경")
    @Test
    void 주문_상태_변경() {
        //given
        Order order = new Order(1L, orderTable1.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());

        Order changedOrder = new Order(1L, orderTable1.getId());
        changedOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(1L)).willReturn(Optional.of(order));

        //when
        Order resultOrder = orderService.changeOrderStatus(1L, changedOrder);

        //then
        assertThat(resultOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());

    }
    @DisplayName("주문의 상태가 완료일 경우 주문의 상태를 변경할 수 없다.")
    @Test
    void 주문의_상태가_완료일시_주문_상태_변경_실패() {
        //given
        Order order = new Order(1L, orderTable1.getId());
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(1L)).willReturn(Optional.of(order));

        //when+then
        assertThrows(IllegalArgumentException.class, ()-> orderService.changeOrderStatus(1L, order));
    }
}
