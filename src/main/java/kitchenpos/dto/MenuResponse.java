package kitchenpos.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MenuResponse {
    private long id;
    private String name;
    private long price;
    private long menuGroupId;

    @Builder
    public MenuResponse(long id, String name, long price, long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuResponse)) return false;
        MenuResponse that = (MenuResponse) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
