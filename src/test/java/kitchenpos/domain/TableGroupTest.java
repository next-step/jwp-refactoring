package kitchenpos.domain;

import kitchenpos.table.domain.TableGroup;

import java.time.LocalDateTime;

public class TableGroupTest {
    public static final TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now());
}