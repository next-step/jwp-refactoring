package kitchenpos.dto;

import kitchenpos.domain.OrderTable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TableGroupResponse {
    private long id;
    private LocalDateTime createdDate;
    private List<TableResponse> tableResponses;

    @Builder
    public TableGroupResponse(long id, LocalDateTime createdDate, List<TableResponse> tableResponses) {
        this.id = id;
        this.createdDate = createdDate;
        this.tableResponses = tableResponses;
    }
}
