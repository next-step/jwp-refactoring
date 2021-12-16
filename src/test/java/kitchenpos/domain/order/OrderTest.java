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
import kitchenpos.application.fixture.OrderLineItemFixtureFactory;
import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.application.fixture.ProductFixtureFactory;
import kitchenpos.application.order.OrderValidator;
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
class OrderTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderValidator orderValidator;

    private OrderTable 주문테이블;
    private MenuGroup 고기_메뉴그룹;
    private Product 돼지고기;
    private Product 공기밥;
    private Menu 불고기;
    private MenuProduct 불고기_돼지고기;
    private MenuProduct 불고기_공기밥;
    private OrderLineItem 불고기_주문항목;
    private Order 불고기_주문;


    @BeforeEach
    void setUp() {
        주문테이블 = OrderTableFixtureFactory.createWithGuests(1L, 1, false);
        주문테이블 = orderTableRepository.save(주문테이블);

        고기_메뉴그룹 = MenuGroupFixtureFactory.create(1L, "고기 메뉴그룹");
        돼지고기 = ProductFixtureFactory.create(1L, "돼지고기", 9_000);
        공기밥 = ProductFixtureFactory.create(2L, "공기밥", 1_000);

        불고기_돼지고기 = MenuProductFixtureFactory.create(1L, 불고기, 돼지고기, 1L);
        불고기_공기밥 = MenuProductFixtureFactory.create(2L, 불고기, 공기밥, 1L);
        불고기 = MenuFixtureFactory.create(1L, "불고기", 10_000, 고기_메뉴그룹, Arrays.asList(불고기_돼지고기, 불고기_공기밥));

        불고기_주문항목 = OrderLineItemFixtureFactory.create(1L, 불고기_주문, 불고기.getId(), 1L);
        불고기_주문 = OrderFixtureFactory.create(1L, 주문테이블.getId(), OrderStatus.COOKING, Arrays.asList(불고기_주문항목));

        고기_메뉴그룹 = menuGroupRepository.save(고기_메뉴그룹);
        돼지고기 = productRepository.save(돼지고기);
        공기밥 = productRepository.save(공기밥);
        불고기 = menuRepository.save(불고기);

        불고기_주문 = orderRepository.save(불고기_주문);
    }

    @DisplayName("Oders 는 OrderTable 로 생성된다.")
    @Test
    void create1() {
        // when & then
        assertThatNoException().isThrownBy(() -> Order.of(주문테이블.getId(), Arrays.asList(불고기_주문항목)));
    }

    @DisplayName("Oders 생성 시, OrderTable 이 존재하지 않으면 예외가 발생한다.")
    @Test
    void create2() {
        // given
        Order order = Order.of(0L, Arrays.asList(불고기_주문항목));

        // when & then
        assertThrows(EntityNotFoundException.class, () -> orderValidator.validateOrder(order));
    }

    @DisplayName("완료된 상태가 아니라면, Order 의 상태를 바꿀 수 있다.")
    @Test
    void changeOrderStatus1() {
        // given
        Order order = Order.of(주문테이블.getId(), Arrays.asList(불고기_주문항목));

        // when
        order.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertTrue(order.getOrderStatus().isMeal());
    }

    @DisplayName("완료된 Order 의 상태를 바꾸면 예외가 발생한다.")
    @Test
    void changeOrderStatus2() {
        // given
        Order order = Order.of(주문테이블.getId(), Arrays.asList(불고기_주문항목));
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> order.changeOrderStatus(OrderStatus.COOKING))
                                            .withMessageContaining("완료된 Order 는 상태를 바꿀 수 없습니다.");
    }
}