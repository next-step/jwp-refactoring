package kitchenpos.application;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.application.helper.ServiceTestHelper;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixtureFactory;
import kitchenpos.fixture.MenuProductFixtureFactory;
import kitchenpos.fixture.OrderLineItemFixtureFactory;
import kitchenpos.fixture.OrderTableFixtureFactory;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {
    @Autowired
    private ServiceTestHelper serviceTestHelper;

    @Autowired
    private OrderService orderService;

    private MenuGroup menuGroup;
    private Product product1;
    private Product product2;
    private Product product3;
    private Menu menu1;
    private Menu menu2;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        menuGroup = serviceTestHelper.메뉴그룹_생성됨("메뉴그룹1");
        product1 = serviceTestHelper.상품_생성됨("상품1", 1000);
        product2 = serviceTestHelper.상품_생성됨("상품2", 2000);
        product3 = serviceTestHelper.상품_생성됨("상품3", 3000);
        MenuProduct menuProduct1 = MenuProductFixtureFactory.createMenuProduct(product1.getId(), 4);
        MenuProduct menuProduct2 = MenuProductFixtureFactory.createMenuProduct(product2.getId(), 2);
        menu1 = serviceTestHelper.메뉴_생성됨(menuGroup, "메뉴1", 4000, Lists.newArrayList(menuProduct1));
        menu2 = serviceTestHelper.메뉴_생성됨(menuGroup, "메뉴2", 4000, Lists.newArrayList(menuProduct2));
        orderTable = serviceTestHelper.비어있지않은테이블_생성됨(3);
    }

    @Test
    void 주문등록() {
        OrderLineItem orderLineItem1 = OrderLineItemFixtureFactory.createOrderLine(menu1.getId(), 3);
        OrderLineItem orderLineItem2 = OrderLineItemFixtureFactory.createOrderLine(menu2.getId(), 3);

        Order savedOrder = serviceTestHelper.주문_생성됨(orderTable.getId(),
                Lists.newArrayList(orderLineItem1, orderLineItem2));

        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.toString());
        assertThat(savedOrder.getOrderTableId()).isEqualTo(orderTable.getId());
        assertThat(savedOrder.getOrderLineItems()).hasSize(2);
    }

    @Test
    void 주문등록_주문항목이_없는경우() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> serviceTestHelper.주문_생성됨(orderTable.getId(), emptyList()));
    }

    @Test
    void 주문등록_주문항목에_표시된_메뉴가_저장되지않은경우() {
        MenuProduct menuProduct = MenuProductFixtureFactory.createMenuProduct(product1.getId(), 1);
        Menu notSavedMenu = MenuFixtureFactory.createMenu(menuGroup, "메뉴", 4000, Lists.newArrayList(menuProduct));
        OrderLineItem orderLineItem = OrderLineItemFixtureFactory.createOrderLine(notSavedMenu.getId(), 3);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> serviceTestHelper.주문_생성됨(orderTable.getId(), Lists.newArrayList(orderLineItem)));
    }

    @Test
    void 주문등록_주문테이블이_없는경우() {
        int numberOfGuests = 3;
        OrderTable notSavedOrderTable = OrderTableFixtureFactory.createNotEmptyOrderTable(numberOfGuests);
        OrderLineItem orderLineItem1 = OrderLineItemFixtureFactory.createOrderLine(menu1.getId(), 3);
        OrderLineItem orderLineItem2 = OrderLineItemFixtureFactory.createOrderLine(menu2.getId(), 3);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> serviceTestHelper.주문_생성됨(notSavedOrderTable.getId(),
                        Lists.newArrayList(orderLineItem1, orderLineItem2)));
    }

    @Test
    void 주문등록_주문테이블이_빈테이블인_경우() {
        OrderTable orderTable = serviceTestHelper.빈테이블_생성됨();
        OrderLineItem orderLineItem1 = OrderLineItemFixtureFactory.createOrderLine(menu1.getId(), 3);
        OrderLineItem orderLineItem2 = OrderLineItemFixtureFactory.createOrderLine(menu2.getId(), 3);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> serviceTestHelper.주문_생성됨(orderTable.getId(),
                        Lists.newArrayList(orderLineItem1, orderLineItem2)));
    }

    @Test
    void 주문목록_조회() {
        OrderLineItem orderLineItem1 = OrderLineItemFixtureFactory.createOrderLine(menu1.getId(), 3);
        OrderLineItem orderLineItem2 = OrderLineItemFixtureFactory.createOrderLine(menu2.getId(), 3);
        OrderLineItem orderLineItem3 = OrderLineItemFixtureFactory.createOrderLine(menu2.getId(), 2);

        Order order1 = serviceTestHelper.주문_생성됨(orderTable.getId(), Lists.newArrayList(orderLineItem1, orderLineItem2));
        Order order2 = serviceTestHelper.주문_생성됨(orderTable.getId(), Lists.newArrayList(orderLineItem3));

        List<Order> orders = orderService.list();
        assertThat(orders).hasSize(2);
        Order foundOrder = findOrderById(order1.getId(), orders);
        assertThat(foundOrder.getOrderLineItems()).hasSize(2);
    }

    private Order findOrderById(Long id, List<Order> orders) {
        return orders.stream()
                .filter(order -> id.equals(order.getId()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Test
    void 주문상태_변경() {
        OrderLineItem orderLineItem1 = OrderLineItemFixtureFactory.createOrderLine(menu1.getId(), 3);
        OrderLineItem orderLineItem2 = OrderLineItemFixtureFactory.createOrderLine(menu2.getId(), 3);
        Order order = serviceTestHelper.주문_생성됨(orderTable.getId(), Lists.newArrayList(orderLineItem1, orderLineItem2));

        Order updatedOrder = serviceTestHelper.주문상태_변경(order.getId(), OrderStatus.MEAL);

        assertThat(updatedOrder.getId()).isEqualTo(order.getId());
        assertThat(updatedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    void 주문상태_변경_이미_계산완료상태인경우() {
        OrderLineItem orderLineItem1 = OrderLineItemFixtureFactory.createOrderLine(menu1.getId(), 3);
        OrderLineItem orderLineItem2 = OrderLineItemFixtureFactory.createOrderLine(menu2.getId(), 3);

        Order order = serviceTestHelper.주문_생성됨(orderTable.getId(), Lists.newArrayList(orderLineItem1, orderLineItem2));
        serviceTestHelper.주문상태_변경(order.getId(), OrderStatus.COMPLETION);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> serviceTestHelper.주문상태_변경(order.getId(), OrderStatus.MEAL));
    }
}
