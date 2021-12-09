package kitchenpos.application.fixture;

import static kitchenpos.application.fixture.OrderTableFixture.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import kitchenpos.domain.TableGroup;

public class TableGroupFixture {
    public static TableGroup 단체_테이블그룹 = new TableGroup();

    static {
        init();
    }

    public static void init() {
        단체_테이블그룹.setId(1L);
        단체_테이블그룹.setOrderTables(Arrays.asList(주문1_단체테이블, 주문2_단체테이블));
        단체_테이블그룹.setCreatedDate(LocalDateTime.now());
    }

    private TableGroupFixture() {}
}
