package kitchenpos.domain.table;

import kitchenpos.domain.Price;
import kitchenpos.domain.Quantity;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.Menus;
import kitchenpos.domain.menuproduct.MenuProduct;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderCreate;
import kitchenpos.domain.order.OrderLineItemCreate;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.exception.TableEmptyException;
import kitchenpos.fixture.CleanUp;
import kitchenpos.fixture.MenuFixture;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.OrderFixture.결제완료1;
import static kitchenpos.fixture.OrderTableFixture.미사용중인_테이블;
import static kitchenpos.fixture.OrderTableFixture.사용중인_1명_테이블;
import static kitchenpos.fixture.ProductFixture.콜라_100원;
import static kitchenpos.fixture.ProductFixture.후라이드치킨_2000원;
import static org.assertj.core.api.Assertions.*;

class OrderTest {

    @BeforeEach
    void setUp() {
        CleanUp.cleanUpOrderFirst();
    }

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    @DisplayName("식사 또는 조리일땐 주문이 진행중이다")
    void 식사_또는_조리일땐_주문이_진행중이다(OrderStatus status) {
        Order order = new Order(null, null, status, null, Lists.emptyList());
        assertThat(order.isFinished()).isFalse();
    }

    @Test
    @DisplayName("결제완료일 땐 주문이 끝난것이다")
    void 결제완료일_땐_주문이_끝난것이다() {
        assertThat(결제완료1.isFinished()).isTrue();
    }

    @Test
    @DisplayName("OrderCreate의 OrderLineItem size와 Menus의 size가 틀리면 IllegalArgumentException이 발생한다")
    void OrderCreate의_OrderLineItem_Size와_Menus의_size가_틀리면_IllegalArugmentException이_발생한다() {
        // given
        OrderCreate orderCreate = new OrderCreate(
                null,
                null,
                Arrays.asList(
                        new OrderLineItemCreate(1, 1),
                        new OrderLineItemCreate(2, 2),
                        new OrderLineItemCreate(3, 3)
                )
        );
        Menus menus = new Menus(Arrays.asList(MenuFixture.후라이드치킨_콜라_2000원_1개, MenuFixture.양념치킨_콜라_1000원_1개));
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> Order.create(orderCreate, menus, 사용중인_1명_테이블));
    }

    @Test
    @DisplayName("빈 테이블이면 TableEmptyException이 발생한다")
    void 빈_테이블이면_TableEmptyException이_발생한다() {
        // when & then
        assertThatExceptionOfType(TableEmptyException.class)
                .isThrownBy(() -> Order.create(null, null, 미사용중인_테이블));
    }

    @Test
    @DisplayName("정상적인 생성")
    void 정상적인_생성() {
        // given
        MenuProduct menuProduct1 = new MenuProduct(후라이드치킨_2000원, 1);
        MenuProduct menuProduct2 = new MenuProduct(콜라_100원, 1);

        OrderCreate orderCreate = new OrderCreate(
                null,
                null,
                Arrays.asList(
                        new OrderLineItemCreate(1L, 1),
                        new OrderLineItemCreate(2L, 2)
                )
        );
        List<Menu> menuList = Arrays.asList(
                new Menu(1L, "1", new Price(1),  null, Arrays.asList(menuProduct1)),
                new Menu(2L, "2", new Price(2), null, Arrays.asList(menuProduct2))
        );
        Menus menus = new Menus(menuList);

        // when
        Order order = Order.create(orderCreate, menus, 사용중인_1명_테이블);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(order.getOrderTable()).isEqualTo(사용중인_1명_테이블);
        assertThat(order.getOrderedTime()).isNotNull();

        assertThat(order.getOrderLineItems())
                .map(item -> item.getMenu())
                .containsExactlyElementsOf(menuList);
        assertThat(order.getOrderLineItems())
                .map(item -> item.getQuantity())
                .containsExactly(new Quantity(1L), new Quantity(2L));
        assertThat(order.getOrderLineItems())
                .map(item -> item.getOrder())
                .containsOnly(order);
    }
}