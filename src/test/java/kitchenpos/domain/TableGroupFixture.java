package kitchenpos.domain;

import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.dto.TableGroupRequest;

public class TableGroupFixture {
    private TableGroupFixture() {
    }

    public static TableGroupRequest tableGroupRequest(List<Long> orderTableId) {
        return new TableGroupRequest(orderTableId);
    }

    public static TableGroup savedTableGroup(Long id) {
        TableGroup tableGroup = mock(TableGroup.class);
        given(tableGroup.getId()).willReturn(id);
        given(tableGroup.getCreatedDate()).willReturn(LocalDateTime.now());
        return tableGroup;
    }
}
