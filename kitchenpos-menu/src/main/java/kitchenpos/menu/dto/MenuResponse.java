package kitchenpos.menu.dto;

import java.util.Objects;

public class MenuResponse {
    private long id;
    private String name;
    private long price;
    private long menuGroupId;

    protected MenuResponse(){}

    public MenuResponse(long id, String name, long price, long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public long getMenuGroupId() {
        return menuGroupId;
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
