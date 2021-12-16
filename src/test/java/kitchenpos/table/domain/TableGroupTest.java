package kitchenpos.table.domain;

import static kitchenpos.table.sample.OrderTableSample.빈_두명_테이블;
import static kitchenpos.table.sample.OrderTableSample.빈_세명_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.common.exception.InvalidStatusException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단체 지정")
class TableGroupTest {

    @Test
    @DisplayName("생성")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> TableGroup.from(Arrays.asList(
                OrderTable.empty(Headcount.from(1)),
                OrderTable.empty(Headcount.from(2))
            )));
    }

    @Test
    @DisplayName("주문 테이블들은 필수")
    void instance_nullOrderTables_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> TableGroup.from(null))
            .withMessage("주문 테이블 리스트는 필수입니다.");
    }

    @Test
    @DisplayName("주문 테이블들은 적어도 2개이상")
    void instance_containNull_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> TableGroup.from(
                Collections.singletonList(OrderTable.empty(Headcount.from(2)))))
            .withMessageEndingWith("개 이상 이어야 합니다.");
    }

    @Test
    @DisplayName("그룹 해제")
    void ungroup() {
        List<OrderTable> orderTables = Arrays.asList(빈_두명_테이블(), 빈_세명_테이블());
        TableGroup tableGroup = TableGroup.from(orderTables);

        //when
        tableGroup.ungroup();

        //then
        assertThat(orderTables)
            .extracting(OrderTable::hasTableGroup)
            .containsExactly(false, false);
    }

    @Test
    @DisplayName("하나라도 주문된 상태라면 그룹 해제 불가능")
    void ungroup_anyCooking_thrownInvalidStatusException() {
        OrderTable 빈_두명_테이블 = 빈_두명_테이블();
        TableGroup tableGroup = TableGroup.from(Arrays.asList(빈_두명_테이블, 빈_세명_테이블()));

        //when
        ThrowingCallable ungroupCallable = tableGroup::ungroup;

        //then
        assertThatExceptionOfType(InvalidStatusException.class)
            .isThrownBy(ungroupCallable)
            .withMessageEndingWith("단체 지정을 해제할 수 없습니다.");
    }
}
