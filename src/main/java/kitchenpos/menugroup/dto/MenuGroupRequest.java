package kitchenpos.menugroup.dto;

public class MenuGroupRequest {

    private String name;

    protected MenuGroupRequest() {}

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
