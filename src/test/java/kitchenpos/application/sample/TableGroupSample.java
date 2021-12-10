package kitchenpos.application.sample;

import java.time.LocalDateTime;
import kitchenpos.domain.TableGroup;

public class TableGroupSample {

    public static TableGroup tableGroup() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }
}
