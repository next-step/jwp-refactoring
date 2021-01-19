package kitchenpos.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TableGroupRequest {
    @NotEmpty
    private List<Long> tableIds;

    public TableGroupRequest(List<Long> tableIds) {
        this.tableIds = tableIds;
    }
}
