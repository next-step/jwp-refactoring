package kitchenpos.domain;

import kitchenpos.exception.TableEmptyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class OrderTest {

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    @DisplayName("식사 또는 조리일땐 주문이 진행중이다")
    void 식사_또는_조리일땐_주문이_진행중이다(OrderStatus status) {
        Order order = new Order(null, null, status, null, null);
        assertThat(order.isFinished()).isFalse();
    }

    @Test
    @DisplayName("결제완료일 땐 주문이 끝난것이다")
    void 결제완료일_땐_주문이_끝난것이다() {
        Order order = new Order(null, null, OrderStatus.COMPLETION, null, null);
        assertThat(order.isFinished()).isTrue();
    }

    @Test
    @DisplayName("OrderCreate의 OrderLineItem size와 Menus의 size가 틀리면 IllegalArgumentException이 발생한다")
    void OrderCreate의_OrderLineItem_Size와_Menus의_size가_틀리면_IllegalArugmentException이_발생한다() {
        // given
        OrderCreate orderCreate = new OrderCreate(
                null,
                null,
                Arrays.asList(
                        new OrderLineItemCreate(0, 0),
                        new OrderLineItemCreate(0, 0),
                        new OrderLineItemCreate(0, 0)
                )
        );
        Menus menus = new Menus(Arrays.asList(new Menu(), new Menu()));
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> Order.create(orderCreate, menus, new OrderTable(null, null, null, null, false)));
    }

    @Test
    @DisplayName("빈 테이블이면 TableEmptyException이 발생한다")
    void 빈_테이블이면_TableEmptyException이_발생한다() {
        // given
        OrderTable orderTable = new OrderTable(null, null, null, null, true);

        // when & then
        assertThatExceptionOfType(TableEmptyException.class)
                .isThrownBy(() -> Order.create(null, null, orderTable));
    }

    @Test
    @DisplayName("정상적인 생성")
    void 정상적인_생성() {
        // given

        Product product = new Product("SIMPLE", new Price(100));

        MenuProduct menuProduct1 = new MenuProduct(null, product, 1);
        MenuProduct menuProduct2 = new MenuProduct(null, product, 1);
        MenuProduct menuProduct3 = new MenuProduct(null, product, 1);
        
        OrderCreate orderCreate = new OrderCreate(
                null,
                null,
                Arrays.asList(
                        new OrderLineItemCreate(1L, 1),
                        new OrderLineItemCreate(2L, 2),
                        new OrderLineItemCreate(3L, 3)
                )
        );
        List<Menu> menuList = Arrays.asList(
                new Menu(1L, "1", new Price(1),  null, Arrays.asList(menuProduct1)),
                new Menu(2L, "2", new Price(2), null, Arrays.asList(menuProduct2)),
                new Menu(3L, "3", new Price(3), null, Arrays.asList(menuProduct3))
        );
        Menus menus = new Menus(menuList);
        OrderTable orderTable = new OrderTable(1L, null, null, null, false);

        // when
        Order order = Order.create(orderCreate, menus, orderTable);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(order.getOrderTable()).isEqualTo(orderTable);
        assertThat(order.getOrderedTime()).isNotNull();

        assertThat(order.getOrderLineItems())
                .map(item -> item.getMenu())
                .containsExactlyElementsOf(menuList);
        assertThat(order.getOrderLineItems())
                .map(item -> item.getQuantity())
                .containsExactly(1L, 2L, 3L);
        assertThat(order.getOrderLineItems())
                .map(item -> item.getOrder())
                .containsOnly(order);
    }
}