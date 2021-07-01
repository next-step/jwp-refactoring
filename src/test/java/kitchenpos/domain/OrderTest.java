package kitchenpos.domain;

import kitchenpos.exception.TableEmptyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

class OrderTest {

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    @DisplayName("식사 또는 조리일땐 주문이 진행중이다")
    void 식사_또는_조리일땐_주문이_진행중이다(String status) {
        Order order = new Order(null, null, status, null, null);
        assertThat(order.isFinished()).isFalse();
    }

    @Test
    @DisplayName("결제완료일 땐 주문이 끝난것이다")
    void 결제완료일_땐_주문이_끝난것이다() {
        Order order = new Order(null, null, OrderStatus.COMPLETION.name(), null, null);
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
}