package kitchenpos.table.domain;

import kitchenpos.table.exception.TableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTablesTest {
    @Test
    @DisplayName("그룹 테이블 생성")
    void create() {
        //given
        final List<OrderTable> orderTables = Arrays.asList(OrderTable.of(0, true), OrderTable.of(0, true));
        // when
        assertThat(OrderTables.of(orderTables)).isInstanceOf(OrderTables.class);
    }

    @Test
    @DisplayName("그룹 테이블 개수 부족 생성 오류")
    void exception() {
        assertThatThrownBy(() -> OrderTables.of(Arrays.asList(OrderTable.of(0, true))))
                .isInstanceOf(TableException.class);
    }

    @Test
    @DisplayName("그룹 테이블 사용중인 테이블 생성 오류")
    void exception2() {
        assertThatThrownBy(() -> OrderTables.of(Arrays.asList(OrderTable.of(0, true), OrderTable.of(0, false))))
                .isInstanceOf(TableException.class);
    }

    @Test
    @DisplayName("그룹 테이블과 요청 테이블 수 오류")
    void exception3() {
        // given
        final List<OrderTable> requestOrderTables = Arrays.asList(OrderTable.of(0, true));
        final OrderTables saveTables = OrderTables.of(Arrays.asList(OrderTable.of(0, true), OrderTable.of(0, true)));
        // then
        assertThatThrownBy(() ->saveTables.notMatchCount(requestOrderTables))
                .isInstanceOf(TableException.class);
    }

    @Test
    @DisplayName("그룹 아이디, empty 업데이트")
    void updateGroupIdAndEmptyStatus() {
        // given
        final OrderTables requestOrderTables = OrderTables.of(Arrays.asList(OrderTable.of(0, true),
                OrderTable.of(0, true)));
        // when
        requestOrderTables.updateGroupTableIdAndEmpty(1L);
        // then
        assertThat(requestOrderTables.get().stream().allMatch(it -> it.getTableGroupId() == 1L))
                .isTrue();
    }


}