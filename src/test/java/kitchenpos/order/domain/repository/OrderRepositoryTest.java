package kitchenpos.order.domain.repository;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuGroupRepositoryTest;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.repository.ProductRepositoryTest;
import kitchenpos.table.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrderRepositoryTest {
    @Autowired
    OrderTableRepository orderTableRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    MenuGroupRepository menuGroupRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    MenuRepository menuRepository;

    private OrderTable 테이블;
    private Menu 햄버거세트_메뉴;

    @BeforeEach
    void setUp() {
        final MenuGroup 햄버거_메뉴 = menuGroupRepository.save(MenuGroupRepositoryTest.햄버거_메뉴.toEntity());
        final Product 불고기버거 = productRepository.save(ProductRepositoryTest.불고기버거.toEntity());
        final Product 새우버거 = productRepository.save(ProductRepositoryTest.새우버거.toEntity());
        테이블 = orderTableRepository.save(OrderTable.of(0, true));

        final Menu 메뉴 = Menu.of("불고기버거", BigDecimal.valueOf(5_000), 햄버거_메뉴);
        햄버거세트_메뉴 = menuRepository.save(메뉴);
        햄버거세트_메뉴.addMenuProducts(Arrays.asList(
                MenuProduct.of(불고기버거.getId(), 5L),
                MenuProduct.of(새우버거.getId(), 1L)));
    }

    @Test
    @DisplayName("주문 생성")
    void create() {
        // given
        final Order 주문생성 = Order.of(테이블.getId(), Arrays.asList(OrderLineItem.of(햄버거세트_메뉴, 1L)));
        // when
        final Order 주문 = orderRepository.save(주문생성);
        // then
        assertThat(주문).isInstanceOf(Order.class);
        assertThat(주문.getOrderLineItems()).hasSize(1);
    }

    @Test
    @DisplayName("테이블 상태 조회")
    void tableStatus() {
        // given
        final Order 주문생성 = Order.of(테이블.getId(), Arrays.asList(OrderLineItem.of(햄버거세트_메뉴, 1L)));
        final Order 주문 = orderRepository.save(주문생성);
        // when
        final Order order = orderRepository.findByOrderTableId(테이블.getId()).get();
        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

}
