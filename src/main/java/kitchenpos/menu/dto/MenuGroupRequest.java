package kitchenpos.menu.dto;

public class MenuGroupRequest {
    private final String name;

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
