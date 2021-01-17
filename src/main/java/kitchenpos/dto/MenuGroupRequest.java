package kitchenpos.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MenuGroupRequest {
    private String name;

    @Builder
    public MenuGroupRequest(String name) {
        this.name = name;
    }
}
