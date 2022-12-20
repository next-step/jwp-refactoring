package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.utils.DatabaseCleanup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(DatabaseCleanup.class)
@DisplayName("주문 관련 도메인 테스트")
public class OrderTest {
    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuRepository menuRepository;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute();
    }

    @DisplayName("주문 생성 테스트")
    @Test
    void createOrder() {
        // given
        Menu menu = createMenuFixture("순살후라이드치킨");
        OrderTable 테이블 = orderTableRepository.save(new OrderTable(4, false));
        OrderLineItem orderLineItem = new OrderLineItem(menu, 2L);

        // when
        Order savedOrder = orderRepository.save(new Order(테이블.getId()));
        savedOrder.addOrderLineItems(Lists.newArrayList(orderLineItem));
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
        OrderTable 테이블 = orderTableRepository.save(new OrderTable(4, false));
        OrderLineItem orderLineItem = new OrderLineItem(menu, 2L);
        Order savedOrder = orderRepository.save(new Order(테이블.getId()));
        savedOrder.addOrderLineItems(Lists.newArrayList(orderLineItem));
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
        Menu menu = new Menu(name, BigDecimal.valueOf(10_000), menuGroup);
        Menu save = menuRepository.save(menu);
        save.addMenuProducts(Lists.newArrayList(new MenuProduct(product, 1L)));

        return save;
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
