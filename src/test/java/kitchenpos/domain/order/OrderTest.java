package kitchenpos.domain.order;

import static kitchenpos.utils.generator.OrderTableFixtureGenerator.비어있는_주문_테이블_생성;
import static kitchenpos.utils.generator.OrderTableFixtureGenerator.비어있지_않은_주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.utils.generator.ScenarioTestFixtureGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Domain:Order")
class OrderTest extends ScenarioTestFixtureGenerator {

    private OrderTable 비어있지_않은_주문_테이블_생성, 비어있는_주문_테이블_생성;

    @BeforeEach
    public void setUp() {
        비어있지_않은_주문_테이블_생성 = 비어있지_않은_주문_테이블_생성();
        비어있는_주문_테이블_생성 = 비어있는_주문_테이블_생성();
    }

    @Test
    @DisplayName("주문 항목이 없는 경우 예외 발생 검증")
    public void throwException_WhenOrderLineItemIsEmpty() {
        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new Order(비어있지_않은_주문_테이블_생성, Collections.emptyList()));
    }

    @Test
    @DisplayName("비어있는 주문 테이블의 주문 생성 시 예외 발생 검증")
    public void throwException_WhenOrderTableIsEmpty() {
        // Given
        OrderLineItem firstOrderLineItem = new OrderLineItem(고기_더블_더블_메뉴, 1);
        OrderLineItem secondOrderLineItem = new OrderLineItem(커플_냉삼_메뉴, 1);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new Order(비어있는_주문_테이블_생성, Arrays.asList(firstOrderLineItem, secondOrderLineItem)));
    }
}
