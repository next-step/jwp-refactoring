package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import org.assertj.core.util.Lists;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TestTableGroupFactory {
    public static TableGroup 테이블_그룹_조회됨(final Long id, final int countOrderTable) {
        final List<OrderTable> orderTables = new ArrayList<>();
        for (int i = 1; i <= countOrderTable; i++) {
            orderTables.add(OrderTable.of(Long.valueOf(i), 10, false));
        }
        return TableGroup.of(id, orderTables);
    }

    public static TableGroup 테이블_그룹_생성됨(final Long id, final int countOrderTable) {
        final List<OrderTable> orderTables = new ArrayList<>();
        for (int i = 1; i <= countOrderTable; i++) {
            orderTables.add(OrderTable.of(id, 10, false));
        }
        return TableGroup.of(id, orderTables);
    }

    public static TableGroup 테이블_그룹_추가됨(final Long id, final OrderTable...orderTables) {
        return TableGroup.of(id, Lists.newArrayList(orderTables));
    }

    public static TableGroupRequest 테이블_그룹_요청(final List<Long> longs) {
        return TableGroupRequest.from(longs);
    }

    public static TableGroupResponse 테이블_그룹_응답(final TableGroup tableGroup) {
        return TableGroupResponse.from(tableGroup);
    }

    public static void 테이블_그룹_생성_확인함(final TableGroupResponse actual, final TableGroup tableGroup) {
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isEqualTo(tableGroup.getId()),
                () -> assertThat(actual.getOrderTableRespons()).hasSize(tableGroup.getOrderTables().toList().size())
        );
    }

    public static void 테이블그룹_해제_확인됨(final OrderTable orderTable1, final OrderTable orderTable2) {
        assertAll(
                () -> assertThat(orderTable1.getTableGroup()).isNull(),
                () -> assertThat(orderTable2.getTableGroup()).isNull()
        );
    }
}
