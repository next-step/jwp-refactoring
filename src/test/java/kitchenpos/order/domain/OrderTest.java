package kitchenpos.order.domain;

import kitchenpos.JpaEntityTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.repository.ProductRepository;
import kitchenpos.order.repository.OrderRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 관련 도메인 테스트")
public class OrderTest extends JpaEntityTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuRepository menuRepository;

    @DisplayName("주문 생성 테스트")
    @Test
    void createOrder() {
        // given
        Menu menu = createMenuFixture("순살후라이드치킨");
        OrderTable 테이블 = new OrderTable(4, false);
        OrderLineItem orderLineItem = new OrderLineItem(menu, 2L);

        // when
        Order savedOrder = orderRepository.save(new Order(테이블, Lists.newArrayList(orderLineItem)));
        flushAndClear();

        // then
        Order order = orderRepository.getOne(savedOrder.getId());

        assertThat(order.status()).isEqualTo(OrderStatus.COOKING);
        assertThat(order.getOrderLineItems()).hasSize(1);
    }


    @DisplayName("주문상태 업데이트 테스트")
    @Test
    void updateOrderStatus() {
        // given
        Menu menu = createMenuFixture("순살후라이드치킨");
        OrderTable 테이블 = new OrderTable(4, false);
        OrderLineItem orderLineItem = new OrderLineItem(menu, 2L);
        Order savedOrder = orderRepository.save(new Order(테이블, Lists.newArrayList(orderLineItem)));
        flushAndClear();

        // when
        Order order = orderRepository.getOne(savedOrder.getId());
        order.updateStatus(OrderStatus.MEAL);
        flushAndClear();

        // then
        order = orderRepository.getOne(savedOrder.getId());
        assertThat(order.status()).isEqualTo(OrderStatus.MEAL);

        // when
        order.updateStatus(OrderStatus.COMPLETION);
        flushAndClear();

        // then
        order = orderRepository.getOne(savedOrder.getId());
        assertThat(order.status()).isEqualTo(OrderStatus.COMPLETION);
    }

    private Menu createMenuFixture(String name) {
        Product product = new Product(name, BigDecimal.valueOf(10_000));
        productRepository.save(product);

        MenuGroup menuGroup = new MenuGroup("한마리치킨");
        Menu menu = new Menu(name, BigDecimal.valueOf(10_000), menuGroup, Lists.newArrayList(new MenuProduct(product, 1L)));
        return menuRepository.save(menu);
    }
}
