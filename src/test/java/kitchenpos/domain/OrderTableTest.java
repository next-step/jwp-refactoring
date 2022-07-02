package kitchenpos.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {
    public static final OrderTable EMPTY_TABLE = new OrderTable(1, true);

    @Test
    void 빈_테이블인지_확인한다() {
        // when & then
        assertThatThrownBy(() ->
                EMPTY_TABLE.validateEmpty()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블은 주문을 할 수 없습니다.");
    }

    @ParameterizedTest
    @MethodSource("그룹_지정할_수_없는_테이블_조회")
    void 빈_테이블이_아니거나_이미_단체가_지정되었으면_단체지정을_할_수_없다(OrderTable orderTable) {
        // when & then
        assertThatThrownBy(() ->
                orderTable.validateCanGroup()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블이 아니거나 이미 단체가 지정되었습니다.");
    }

    private static Stream<Arguments> 그룹_지정할_수_없는_테이블_조회() {
        return Stream.of(
                Arguments.of(
                        new OrderTable(1, false)
                ),
                Arguments.of(
                        createAlreadyGroupedTable()
                )
        );
    }

    private static OrderTable createAlreadyGroupedTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.updateTableGroupId(1L);
        return orderTable;
    }
}