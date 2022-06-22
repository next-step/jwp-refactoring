package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
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

    private Product product;
    private List<MenuProduct> menuProducts;
    private MenuGroup menuGroup;
    private Menu menu;

    @BeforeEach
    void beforeEach() {
        product = new Product.Builder("소불고기", 1000).id(1L).build();
        menuProducts = Arrays.asList(new MenuProduct.Builder(product.getId(), 1).build(),
                new MenuProduct.Builder(product.getId(), 1).build());
        menuGroup = new MenuGroup.Builder().id(1L).name("점심메뉴").build();
        menu = new Menu.Builder("점심특선", 2000, menuGroup.getId(), menuProducts).build();
    }

    @Test
    void create() {
        // given
        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(new OrderTable.Builder(4, false).build()));
        given(orderDao.save(any(Order.class))).willReturn(new Order.Builder().id(1L).orderStatus(OrderStatus.COOKING.name()).build());
        given(orderLineItemDao.save(any(OrderLineItem.class))).willReturn(new OrderLineItem.Builder().build());

        // when
        Order created = orderService.create(new Order.Builder(1L,
                Arrays.asList(new OrderLineItem.Builder(menu.getId(), 1).build())).build());

        // then
        assertThat(created.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());

        // verify
        then(menuDao).should(times(1)).countByIdIn(anyList());
        then(orderTableDao).should(times(1)).findById(anyLong());
        then(orderDao).should(times(1)).save(any(Order.class));
        then(orderLineItemDao).should(times(1)).save(any(OrderLineItem.class));
    }

    @Test
    void changeOrderStatus() {
        // given
        Order order = new Order.Builder().id(1L).orderStatus(OrderStatus.COOKING.name()).build();
        Order status = new Order.Builder().orderStatus(OrderStatus.MEAL.name()).build();

        given(orderDao.findById(anyLong())).willReturn(Optional.of(order));
        given(orderLineItemDao.findAllByOrderId(anyLong())).willReturn(Arrays.asList(new OrderLineItem.Builder().build()));

        // when
        Order changed = orderService.changeOrderStatus(order.getId(), status);

        // then
        assertThat(changed.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());

        // verify
        then(orderDao).should(times(1)).findById(anyLong());
        then(orderDao).should(times(1)).save(any(Order.class));
        then(orderLineItemDao).should(times(1)).findAllByOrderId(anyLong());
    }
}
