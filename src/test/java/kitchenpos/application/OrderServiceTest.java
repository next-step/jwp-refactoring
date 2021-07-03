package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    MenuDao menuDao;
    @Mock
    OrderDao orderDao;
    @Mock
    OrderLineItemDao orderLineItemDao;
    @Mock
    OrderTableDao orderTableDao;

    OrderService orderService;

    Order order;

    @BeforeEach
    void setUp() {
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1L);

        order = new Order(1L, Arrays.asList(orderLineItem1, orderLineItem2));

        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
    }

    @DisplayName("주문이 발생함")
    @Test
    void 주문이_발생함() {
        //given
        given(menuDao.countByIdIn(anyList())).willReturn(2L);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(new OrderTable(1L, 1L, 4, false)));

        given(orderDao.save(order)).willReturn(new Order(1L, order.getOrderTableId(), OrderStatus.COOKING.name()));

        //when
        Order savedOrder = orderService.create(order);

        //then
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @DisplayName("주문 항목이 아무것도 입력이 안되었을 경우")
    @Test
    void 주문_항목이_아무것도_입력이_안되었을_경우() {
        //given
        order = new Order(1L, Collections.emptyList());

        //when, then
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목의 메뉴 중 등록되지 않은 메뉴가 있을 때")
    @Test
    void 주문_항목의_메뉴_중_등록되지_않은_메뉴가_있을_때() {
        //when
        given(menuDao.countByIdIn(anyList())).willReturn(1L);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문에, 존재하지 않는 주문테이블을 입력한 경우")
    @Test
    void 주문에_존재하지_않는_주문테이블을_입력한_경우() {
        //given
        given(menuDao.countByIdIn(anyList())).willReturn(2L);
        given(orderTableDao.findById(1L)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어 있어서 주문이 나갈 수 없는 경우")
    @Test
    void 주문테이블이_비어있는_경우() {
        //given
        given(menuDao.countByIdIn(anyList())).willReturn(2L);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(new OrderTable(1L, 1L, 0, true)));

        //when, then
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 리스트 가져오기")
    @Test
    void 주문테이블_리스트_가져오기() {
        //given
        given(orderDao.findAll()).willReturn(Arrays.asList(
                new Order(1L, 1L, OrderStatus.COOKING.name()),
                new Order(2L, 2L, OrderStatus.COOKING.name())
        ));

        given(orderLineItemDao.findAllByOrderId(1L)).willReturn(
                Arrays.asList(
                        new OrderLineItem(1L, 1L, 1L, 1L),
                        new OrderLineItem(2L, 1L, 2L, 1L)
                )
        );

        given(orderLineItemDao.findAllByOrderId(2L)).willReturn(
                Arrays.asList(
                        new OrderLineItem(3L, 2L, 1L, 1L),
                        new OrderLineItem(4L, 2L, 2L, 1L)
                )
        );

        //when, then
        assertThat(orderService.list()).hasSize(2);
    }

    @DisplayName("주문상태 변경")
    @Test
    void 주문상태_변경() {
        //given
        given(orderDao.findById(1L)).willReturn(Optional.of(new Order(1L, 1L, OrderStatus.COOKING.name())));
        given(orderLineItemDao.findAllByOrderId(1L)).willReturn(Arrays.asList(
                new OrderLineItem(1L, 1L, 1L, 1L),
                new OrderLineItem(2L, 1L, 2L, 1L)
        ));

        //when
        Order order = orderService.changeOrderStatus(1L, new Order(null, null, OrderStatus.MEAL.name()));

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문상태 변경시 이미 계산 완료된 주문")
    @Test
    void 주문상태_변경시_이미_계산_완료된_주문() {
        //given
        given(orderDao.findById(1L)).willReturn(Optional.of(new Order(1L, 1L, OrderStatus.COMPLETION.name())));

        //when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new Order(null, null, OrderStatus.MEAL.name())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문상태 변경시 주문번호가 없는 경우")
    @Test
    void 주문상태_변경시_주문번호가_없는_경우() {
        //when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new Order(null, null, OrderStatus.MEAL.name())))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
