package kitchenpos.domain.tablegroup;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.Collections;
import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.exception.EmptyOrderTablesException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

class OrderTablesTest {

    @DisplayName("OrderTables를 생성할 수 있다.")
    @Test
    void create01() {
        // given
        List<OrderTable> orderTables = Lists.newArrayList();
        orderTables.add(OrderTable.of(true, 1));
        orderTables.add(OrderTable.of(false, 2));

        // when & then
        assertThatNoException().isThrownBy(() -> OrderTables.from(orderTables));
    }

    @DisplayName("OrderTables 생성 시 OrderTable Collection이 없으면 예외가 발생한다.")
    @ParameterizedTest
    @NullSource
    void create02(List<OrderTable> orderTables) {
        // given & when & then
        assertThatExceptionOfType(EmptyOrderTablesException.class)
                .isThrownBy(() -> OrderTables.from(orderTables));
    }
}