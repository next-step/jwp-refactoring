package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemTest {

    @Test
    @DisplayName("메뉴가 없으면 주문 항목을 생성할 수 없다.")
    void noMenu() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> OrderLineItem.of(null, 1)
        );
    }

    @Test
    @DisplayName("순서가 변경 된다.")
    void changeSeq() {
        //given
        OrderLineItem orderLineItem = OrderLineItem.of(1L, 2);

        //when
        orderLineItem.changeSeq(3);

        //then
        assertThat(orderLineItem.getSeq()).isEqualTo(3L);
    }

}