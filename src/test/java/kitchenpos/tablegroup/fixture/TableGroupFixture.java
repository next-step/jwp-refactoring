package kitchenpos.tablegroup.fixture;

import kitchenpos.tablegroup.domain.TableGroup;

import java.time.LocalDateTime;

public class TableGroupFixture {

    public static TableGroup 단체_주문_테이블_그룹 = create(
            1L,
            LocalDateTime.now()
    );

    public static TableGroup create(Long id, LocalDateTime createdDate) {
        return TableGroup.of(id, createdDate);
    }
}
