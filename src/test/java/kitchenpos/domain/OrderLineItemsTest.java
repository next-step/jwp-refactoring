package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {

    @Test
    @DisplayName("주문 목록은 한개 이상 존재해야 합니다.")
    public void validSize(){
        //given
        List<OrderLineItem> orderLineItems = new ArrayList<>();

        //when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> OrderLineItems.from(orderLineItems)
        );
    }

    @Test
    @DisplayName("주문 목록은 존재해야 합니다.")
    public void validNull(){
        //when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> OrderLineItems.from(null)
        );
    }

}