package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TestTableGroupFactory {
    public static TableGroup 테이블_그룹_조회됨(final Long id) {
        return TableGroup.from(id);
    }

    public static TableGroup 테이블_그룹_생성됨(final Long id) {
        return TableGroup.from(id);
    }

    public static TableGroup 테이블_그룹_추가됨(final Long id) {
        return TableGroup.from(id);
    }

    public static TableGroupRequest 테이블_그룹_요청(final List<Long> longs) {
        return TableGroupRequest.from(longs);
    }

    public static TableGroupResponse 테이블_그룹_응답(final TableGroup tableGroup, final List<OrderTable> orderTables) {
        return TableGroupResponse.of(tableGroup, orderTables);
    }

    public static void 테이블_그룹_생성_확인함(final TableGroupResponse actual, final TableGroup tableGroup) {
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isEqualTo(tableGroup.getId()),
                () -> assertThat(actual.getOrderTableRespons()).hasSizeGreaterThan(0)
        );
    }

    public static void 테이블그룹_해제_확인됨(final OrderTable orderTable1, final OrderTable orderTable2) {
        assertAll(
                () -> assertThat(orderTable1.getTableGroupId()).isNull(),
                () -> assertThat(orderTable2.getTableGroupId()).isNull()
        );
    }
}
