package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TableGroupFixture {

    public static TableGroup 단체_지정_데이터_생성(List<OrderTable> orderTables) {
        return new TableGroup(null, null, orderTables);
    }

    public static TableGroup 단체_데이터_생성(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        return new TableGroup(id, createdDate, orderTables);
    }

    public static void 단체_데이터_확인(TableGroup tableGroup, Long tableGroupId, LocalDateTime createdDate) {
        assertAll(
                () -> assertEquals(tableGroupId, tableGroup.getId()),
                () -> assertEquals(createdDate, tableGroup.getCreatedDate())
        );
    }
}
