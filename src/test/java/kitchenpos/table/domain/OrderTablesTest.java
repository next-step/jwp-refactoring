package kitchenpos.table.domain;

import static kitchenpos.exception.KitchenposExceptionMessage.EMPTY_GUESTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import kitchenpos.exception.KitchenposException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTablesTest {

    @DisplayName("제네릭을 사용한 변환 테스트")
    @Test
    void convertAllTest() {
        // given
        OrderTables orderTables = new OrderTables();
        orderTables.addAll(Arrays.asList(new OrderTable(3, false),
                                         new OrderTable(10, false)));

        // when
        assertThat(orderTables.convertAll(OrderTable::getNumberOfGuests))
            .isNotEmpty()
            .containsExactly(3, 10);
    }

    @DisplayName("내부 원소들이 비었거나, 이미 테이블 그룹에 소속되어있는지 체크 테스트")
    @Test
    void checkEmptyAndNotIncludeTableGroupTest() {
        // given
        OrderTables orderTables = new OrderTables();
        orderTables.addAll(Arrays.asList(new OrderTable(3, false),
                                         new OrderTable(10, false)));

        assertThatCode(orderTables::checkEmptyAndNotIncludeTableGroup)
            .doesNotThrowAnyException();
    }

    @DisplayName("내부 원소들이 비었을때 체크 테스트")
    @Test
    void checkNotEmptyAndNotIncludeTableGroupTest() {
        // given
        OrderTables orderTables = new OrderTables();
        orderTables.addAll(Arrays.asList(new OrderTable(0, true),
                                         new OrderTable(10, false)));

        assertThatThrownBy(orderTables::checkEmptyAndNotIncludeTableGroup)
            .isInstanceOf(KitchenposException.class)
            .hasMessageContaining(EMPTY_GUESTS.getMessage());
    }
}
