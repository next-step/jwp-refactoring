package kitchenpos.menu.dto;

public class MenuGroupRequest {

    private String name;

    private MenuGroupRequest() {
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

