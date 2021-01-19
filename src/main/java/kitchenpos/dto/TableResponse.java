package kitchenpos.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TableResponse {
    private long id;
    private Long tableGroupId;
    private int numberOfGuests;

    @Builder
    private TableResponse(long id, Long tableGroupId, int numberOfGuests) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
    }
}
