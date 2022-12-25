package kitchenpos.table.domain;

import kitchenpos.exception.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("주문 테이블이 비어있는지 여부 단위 테스트")
class OrderEmptyTest {

    @DisplayName("방문자 수가 동일하면 주문 방문자는 동일하다.")
    @Test
    void 방문자_수가_동일하면_주문_방문자는_동일하다() {
        assertEquals(
                new OrderEmpty(false),
                new OrderEmpty(false)
        );
    }

    @DisplayName("테이블 그룹을 생성할때 비어있지 않은 주문 테이블을 포함할 수 없다.")
    @Test
    void 테이블_그룹을_생성할때_비어있지_않은_주문_테이블을_포함할_수_없다() {
        OrderEmpty orderEmpty = new OrderEmpty(false);

        assertThatIllegalArgumentException()
                .isThrownBy(orderEmpty::validateForTableGrouping)
                .withMessage(ErrorMessage.ORDER_TABLE_NON_EMPTY_ORDER_TABLE_CANNOT_BE_INCLUDED_IN_TABLE_GROUP.getMessage());
    }
}
