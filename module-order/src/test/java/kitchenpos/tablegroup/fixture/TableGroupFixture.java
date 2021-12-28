package kitchenpos.tablegroup.fixture;

import kitchenpos.tablegroup.domain.TableGroup;

import java.time.LocalDateTime;

public class TableGroupFixture {

    private TableGroupFixture() {
        throw new UnsupportedOperationException();
    }

    public static TableGroup create(Long id, LocalDateTime createdDate) {
        return TableGroup.of(id, createdDate);
    }
}
