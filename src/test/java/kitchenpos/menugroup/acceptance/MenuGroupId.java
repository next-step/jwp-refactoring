package kitchenpos.menugroup.acceptance;

import java.util.Objects;

public class MenuGroupId {
    private final long menuGroupId;

    public MenuGroupId(int menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public long value() {
        return menuGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MenuGroupId that = (MenuGroupId)o;
        return menuGroupId == that.menuGroupId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuGroupId);
    }
}
