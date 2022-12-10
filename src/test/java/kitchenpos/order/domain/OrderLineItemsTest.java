package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderLineItemTestFixture.generateOrderLineItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.common.constant.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 항목 집합 관련 도메인 테스트")
public class OrderLineItemsTest {

    private OrderLineItem 치킨버거세트_주문_항목;
    private OrderLineItem 불고기버거세트_주문_항목;

    @BeforeEach
    void setUp() {
        치킨버거세트_주문_항목 = generateOrderLineItem(1L, 1L, 2L, 2);
        불고기버거세트_주문_항목 = generateOrderLineItem(2L, 1L, 3L, 1);
    }

    @DisplayName("주문 항목 집합을 생성한다.")
    @Test
    void createOrderLineItems() {
        // when
        OrderLineItems orderLineItems = OrderLineItems.from(Arrays.asList(치킨버거세트_주문_항목, 불고기버거세트_주문_항목));

        // then
        assertAll(
                () -> assertThat(orderLineItems.unmodifiableOrderLineItems()).hasSize(2),
                () -> assertThat(orderLineItems.unmodifiableOrderLineItems()).containsExactly(치킨버거세트_주문_항목, 불고기버거세트_주문_항목)
        );
    }

    @DisplayName("주문 항목이 비어있으면 주문 항목 집합 생성 시 에러가 발생한다.")
    @Test
    void createOrderLineItemsThrowErrorWhenOrderLineItemIsEmpty() {
        // when & then
        assertThatThrownBy(() -> OrderLineItems.from(Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.주문_항목은_비어있을_수_없음.getErrorMessage());
    }
}
