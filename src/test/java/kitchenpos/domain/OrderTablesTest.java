package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderTablesTest {
    @Test
    @DisplayName("하나라도 Empty가 true이면 예약이 되어있다")
    void 하나라도_Empty가_True_이면_예약이_되어있다() {
        OrderTables orderTables = new OrderTables(
                Arrays.asList(
                        new OrderTable(1L, null, 1, false),
                        new OrderTable(1L, null, 1, true)
                )
        );

        assertThat(orderTables.isBookedAny()).isTrue();
    }

    @Test
    @DisplayName("하나라도 TableGroup이 지정되있으면 예약이 되어있다")
    void 하나라도_TableGroup이_지정되어있으면_예약이_되어있다() {
        OrderTables orderTables = new OrderTables(
                Arrays.asList(
                        new OrderTable(1L, 1L, 1, true),
                        new OrderTable(1L, null, 1, true)
                )
        );

        assertThat(orderTables.isBookedAny()).isTrue();
    }

    @Test
    @DisplayName("TableGroup이 지정이 안되어있고, 빈 테이블이면 예약이 안되어있다.")
    void TableGroup이_지정이_안되어있고_빈_테이블이면_예약이_안되어있다() {
        OrderTables orderTables = new OrderTables(
                Arrays.asList(
                        new OrderTable(1L, null, 1, true),
                        new OrderTable(1L, null, 1, true)
                )
        );

        assertThat(orderTables.isBookedAny()).isFalse();
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5})
    @DisplayName("개수에 맞게 size를 리턴한다")
    void size(int len) {
        List<OrderTable> orderTableList = new ArrayList<>();
        for (int i = 0; i<len; i++) {
            orderTableList.add(new OrderTable(null, null, 0, false));
        }

        assertThat(new OrderTables(orderTableList).size()).isEqualTo(len);
    }
}