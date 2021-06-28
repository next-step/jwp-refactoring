package kitchenpos.application;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    private OrderLineItem item;

    @BeforeEach
    void setUp() {
        item = new OrderLineItem();
        item.setMenuId(1L);
        item.setQuantity(1);
    }

    @DisplayName("create order 실패 - orderLineItems 가 비어 있음")
    @Test
    void createFail01() {
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(new Order()));
    }

    @DisplayName("create order 실패 - orderLineItems에 menu id가 중복인 데이터가 있음")
    @Test
    void createFail02() {
        // given
        Order order = new Order();
        order.setOrderLineItems(Stream.generate(() -> item)
                                      .limit(5)
                                      .collect(toList()));

        given(menuDao.countByIdIn(any())).willReturn(0L);

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("create order 실패 - order table id를 찾을 수 없음")
    @Test
    void createFail03() {
        // given
        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(item));
        order.setOrderTableId(1L);

        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableRepository.findById(order.getOrderTableId())).willReturn(Optional.empty());

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("create order 실패 - order table의 상태가 empty")
    @Test
    void createFail04() {
        // given
        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(item));
        order.setOrderTableId(1L);

        OrderTable orderTable = new OrderTable();
        orderTable.empty();

        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableRepository.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("create order 성공")
    @Test
    void createSuccess() {
        // given
        OrderLineItem item2 = new OrderLineItem();
        item2.setMenuId(2L);
        item2.setQuantity(1);

        Order order = new Order();
        order.setOrderLineItems(Lists.newArrayList(item, item2));
        order.setOrderTableId(1L);

        OrderTable orderTable = new OrderTable();

        given(menuDao.countByIdIn(any())).willReturn(2L);
        given(orderTableRepository.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));
        given(orderDao.save(order)).willReturn(order);

        // when
        orderService.create(order);

        // then
        verify(orderLineItemDao, times(order.getOrderLineItems().size())).save(any());
    }

    @DisplayName("주문 상태 변경 실패 - order id를 찾을 수 없음")
    @Test
    void changeOrderStatusFail01() {
        // given
        given(orderDao.findById(any())).willReturn(Optional.empty());

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(1L, new Order()));
    }

    @DisplayName("주문 상태 변경 실패 - 완료된 주문은 주문 상태 변경 불가능")
    @Test
    void changeOrderStatusFail02() {
        // given
        Order savedOrder = new Order();
        savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());

        given(orderDao.findById(any())).willReturn(Optional.of(savedOrder));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(1L, new Order()));
    }

    @DisplayName("주문 상태 변경 성공")
    @Test
    void changeOrderStatusSuccess() {
        // given
        Order savedOrder = new Order();
        savedOrder.setOrderStatus(OrderStatus.COOKING.name());

        Order order = new Order();
        order.setOrderStatus(OrderStatus.MEAL.name());

        given(orderDao.findById(any())).willReturn(Optional.of(savedOrder));

        // when
        orderService.changeOrderStatus(1L, order);

        // then
        verify(orderDao).save(any());
        verify(orderLineItemDao).findAllByOrderId(any());
    }
}
