package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;

import kitchenpos.application.fixture.MenuFixtureFactory;
import kitchenpos.application.fixture.MenuGroupFixtureFactory;
import kitchenpos.application.fixture.MenuProductFixtureFactory;
import kitchenpos.application.fixture.OrderFixtureFactory;
import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.application.fixture.ProductFixtureFactory;
import kitchenpos.application.order.OrderValidator;
import kitchenpos.domain.Quantity;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Component.class))
class OrderLineItemTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderValidator orderValidator;

    private MenuGroup 고기_메뉴그룹;
    private Product 돼지고기;
    private Product 공기밥;
    private Menu 불고기;
    private MenuProduct 불고기_돼지고기;
    private MenuProduct 불고기_공기밥;
    private OrderTable 주문_개인테이블;
    private Order 불고기_주문;

    @BeforeEach
    void setUp() {
        고기_메뉴그룹 = MenuGroupFixtureFactory.create(1L, "고기 메뉴그룹");
        돼지고기 = ProductFixtureFactory.create(1L, "돼지고기", 9_000);
        공기밥 = ProductFixtureFactory.create(2L, "공기밥", 1_000);

        불고기_돼지고기 = MenuProductFixtureFactory.create(1L, 불고기, 돼지고기, 1L);
        불고기_공기밥 = MenuProductFixtureFactory.create(2L, 불고기, 공기밥, 1L);
        불고기 = MenuFixtureFactory.create(1L, "불고기", 10_000, 고기_메뉴그룹, Arrays.asList(불고기_돼지고기, 불고기_공기밥));

        주문_개인테이블 = OrderTableFixtureFactory.create(1L, false);
        불고기_주문 = OrderFixtureFactory.create(1L, 주문_개인테이블.getId(), OrderStatus.COOKING);

        고기_메뉴그룹 = menuGroupRepository.save(고기_메뉴그룹);
        돼지고기 = productRepository.save(돼지고기);
        공기밥 = productRepository.save(공기밥);
        불고기 = menuRepository.save(불고기);

        주문_개인테이블 = orderTableRepository.save(주문_개인테이블);
        불고기_주문 = orderRepository.save(불고기_주문);
    }

    @DisplayName("OrderLineItem 는 Menu, Orders, Quantity 로 생성된다.")
    @Test
    void creat1() {
        // when
        OrderLineItem 불고기_주문항목 = OrderLineItem.of(불고기.getId(), 1L);
        불고기_주문.addOrderLineItems(Arrays.asList(불고기_주문항목));

        // then
        assertAll(
            () -> assertEquals(불고기_주문항목.getOrder().getId(), 불고기_주문.getId()),
            () -> assertEquals(불고기_주문항목.getMenuId(), 불고기.getId()),
            () -> assertEquals(불고기_주문항목.getQuantity(), Quantity.from(1L))
        );
    }

    @DisplayName("OrderLineItem 생성 시, Menu 가 존재하지 않으면 예외가 발생한다.")
    @Test
    void creat2() {
        // given
        OrderLineItem orderLineItem = OrderLineItem.of(0L, 1L);
        // when & then
        assertThrows(EntityNotFoundException.class, () -> orderValidator.validateOrderLineItem(orderLineItem));
    }

    @DisplayName("OrderLineItem 생성 시, Order 가 존재하지 않으면 예외가 발생한다.")
    @Test
    void creat3() {
        // given
        OrderLineItem orderLineItem = OrderLineItem.of(1L, null, 불고기.getId(), 1L);

        // when & then
        assertThrows(EntityNotFoundException.class, () -> orderValidator.validateOrderLineItem(orderLineItem));
    }
}