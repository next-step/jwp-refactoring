package kitchenpos.dto;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class TableGroupRequest {
    @NotEmpty
    private List<Long> tableIds;

    protected TableGroupRequest(){}

    public TableGroupRequest(List<Long> tableIds) {
        this.tableIds = tableIds;
    }

    public List<Long> getTableIds() {
        return tableIds;
    }
}
