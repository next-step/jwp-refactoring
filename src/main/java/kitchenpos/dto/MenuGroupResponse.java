package kitchenpos.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MenuGroupResponse {
    private long id;
    private String name;

    @Builder
    public MenuGroupResponse(long id, String name) {
        this.id = id;
        this.name = name;
    }
}

