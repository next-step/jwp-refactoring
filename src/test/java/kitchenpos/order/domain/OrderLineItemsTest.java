package kitchenpos.order.domain;

import static kitchenpos.order.sample.OrderLineItemMenuSample.이십원_후라이트치킨_두마리세트_주문_메뉴;
import static kitchenpos.product.sample.MenuSample.이십원_후라이드치킨_두마리세트;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.Collections;
import kitchenpos.common.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 항목들")
class OrderLineItemsTest {

    @Test
    @DisplayName("생성")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> OrderLineItems.from(
                Collections.singletonList(
                    OrderLineItem.of(Quantity.from(1L), 이십원_후라이트치킨_두마리세트_주문_메뉴()))));
    }

    @Test
    @DisplayName("주문 항목 리스트 필수")
    void instance_nullOrderLineItem_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> OrderLineItems.from(null))
            .withMessage("주문 항목 리스트는 필수입니다.");
    }

    @Test
    @DisplayName("주문 항목 리스트에 null이 포함될 수 없음")
    void instance_containNull_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> OrderLineItems.from(Collections.singletonList(null)))
            .withMessageEndingWith("null이 포함될 수 없습니다.");
    }
}
