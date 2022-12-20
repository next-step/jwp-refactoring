package kitchenpos.menu.dto;

public class MenuGroupCreateRequest {

    private String name;

    public MenuGroupCreateRequest(String name) {
        this.name = name;
    }

    protected MenuGroupCreateRequest() {
    }

    public String getName() {
        return name;
    }
}
