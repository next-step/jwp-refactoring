package kitchenpos.order;

import kitchenpos.order.domain.OrderLineItems;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderLineItemsTest {

    @DisplayName("빈 리스트로 OrderLineItems 생성 불가 테스트")
    @Test
    void createOrderLineItemsEmptyListException() {
        assertThatThrownBy(() -> new OrderLineItems(Arrays.asList()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
