
package kitchenpos.table;

import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTablesTest {

    @DisplayName("주문테이블목록 최소 숫자 이하로 생성 오류 발생 테스트")
    @Test
    void createOrderTablesUnderMinimumExceptionTest() {
        final OrderTable 주문테이블1 = new OrderTable(1L, null, new NumberOfGuests(4), true);

        assertThatThrownBy(() -> new OrderTables(Arrays.asList(주문테이블1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블목록 빈목록으로 생성 오류 발생 테스트")
    @Test
    void createOrderTablesEmptyListExceptionTest() {
        assertThatThrownBy(() -> new OrderTables(Arrays.asList()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
