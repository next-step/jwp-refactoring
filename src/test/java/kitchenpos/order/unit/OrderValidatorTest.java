package kitchenpos.order.unit;

import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("주문요청검증기능")
public class OrderValidatorTest {

    @Test
    @DisplayName("제품 아이템은 비어있으면 안된다.")
    void orderValidatorTest1() {
        OrderRequest orderRequest = new OrderRequest(null, Arrays.asList());

        assertThatThrownBy(() -> OrderValidator.validateParam(orderRequest.getOrderLineItems()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하는 메뉴로만 요청해야한다.")
    void orderValidatorTest2() {
        OrderRequest orderRequest = new OrderRequest(null, Arrays.asList(new OrderLineItemRequest(1L, 1L)));

        assertThatThrownBy(() -> OrderValidator.validateMenus(orderRequest.getOrderLineItems(), 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
