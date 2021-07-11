package kitchenpos.domain.table;

import kitchenpos.domain.Quantity;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.Menus;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderCreate;
import kitchenpos.domain.order.OrderLineItemCreate;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.fixture.CleanUp;
import kitchenpos.fixture.OrderFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.fixture.MenuFixture.양념치킨_콜라_1000원_1개;
import static kitchenpos.fixture.MenuFixture.후라이드치킨_콜라_2000원_1개;
import static kitchenpos.fixture.OrderFixture.결제완료1;
import static kitchenpos.fixture.OrderTableFixture.사용중인_1명_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class OrderTest {

    @BeforeEach
    void setUp() {
        CleanUp.cleanUp();
    }

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    @DisplayName("식사 또는 조리일땐 주문이 진행중이다")
    void 식사_또는_조리일땐_주문이_진행중이다(OrderStatus status) {
        List<OrderLineItemCreate> creates = Arrays.asList(new OrderLineItemCreate(1L, new Quantity(1)));
        OrderCreate orderCreate = new OrderCreate(null, status, creates);
        Order order = Order.createOrder(orderCreate, OrderFixture.주문한개_메뉴);
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
                        new OrderLineItemCreate(1, new Quantity(1)),
                        new OrderLineItemCreate(2, new Quantity(2)),
                        new OrderLineItemCreate(3, new Quantity(3))
                )
        );
        Menus menus = new Menus(Arrays.asList(후라이드치킨_콜라_2000원_1개, 양념치킨_콜라_1000원_1개));
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> Order.createOrder(사용중인_1명_테이블.getId(), orderCreate, menus));
    }

    @Test
    @DisplayName("정상적인 생성")
    void 정상적인_생성() {
        // given
        OrderCreate orderCreate = new OrderCreate(
                사용중인_1명_테이블.getId(),
                OrderStatus.COOKING,
                Arrays.asList(
                        new OrderLineItemCreate(후라이드치킨_콜라_2000원_1개.getId(), new Quantity(1)),
                        new OrderLineItemCreate(양념치킨_콜라_1000원_1개.getId(), new Quantity(2))
                )
        );
        List<Menu> menuList = Arrays.asList(후라이드치킨_콜라_2000원_1개, 양념치킨_콜라_1000원_1개);
        Menus menus = new Menus(menuList);
        List<Long> menuIds = menuList.stream()
                .map(Menu::getId)
                .collect(Collectors.toList());

        // when
        Order order = Order.createOrder(2L, orderCreate, menus);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(order.getOrderTableId()).isEqualTo(사용중인_1명_테이블.getId());
        assertThat(order.getOrderedTime()).isNotNull();

        assertThat(order.getOrderLineItems())
                .map(item -> item.getMenuId())
                .containsExactlyElementsOf(menuIds);
        assertThat(order.getOrderLineItems())
                .map(item -> item.getQuantity())
                .containsExactly(new Quantity(1L), new Quantity(2L));
    }
}