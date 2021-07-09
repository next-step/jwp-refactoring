package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private Order order;
    private OrderLineItem orderLineItem;
    private OrderLineItem orderLineItem2;
    private OrderTable orderTable;

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

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);
        order.setOrderTableId(1L);

        orderLineItem = new OrderLineItem();
        orderLineItem2 = new OrderLineItem();

        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setTableGroupId(1L);
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);
    }

    @DisplayName("주문을 등록한다")
    @Test
    void create() {
        // given
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem);
        given(menuDao.countByIdIn(any())).willReturn((long)orderLineItems.size());
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));
        given(orderDao.save(order)).willReturn(order);

        // when
        order.setOrderLineItems(orderLineItems);

        Order actual = orderService.create(this.order);

        // then
        assertThat(actual).isEqualTo(order);
    }

    @DisplayName("주문을 등록한다 - 메뉴 정보는 필수 입력사항이다")
    @Test
    void create_menuIsEssentialInput() {
        // given when
        order.setOrderLineItems(Collections.emptyList());

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(this.order));
    }

    @DisplayName("주문을 등록한다 - 테이블 정보는 필수 입력사항이다")
    @Test
    void create_tableIsEssentialInput() {
        // given
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem);
        orderLineItems.add(orderLineItem2);
        given(menuDao.countByIdIn(any())).willReturn((long)orderLineItems.size());
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.empty());

        // when
        order.setOrderLineItems(orderLineItems);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(this.order));
    }

    @DisplayName("주문을 등록한다 - 동일한 메뉴정보는 중복 입력할 수 없다")
    @Test
    void create_duplicateMenu() {
        // given
        given(menuDao.countByIdIn(any())).willReturn(1L);

        // when
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem);
        orderLineItems.add(orderLineItem2);
        order.setOrderLineItems(orderLineItems);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(this.order));
    }

}