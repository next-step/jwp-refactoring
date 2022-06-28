package kitchenpos.menugroup.dto;

public class MenuGroupRequest {

    private String name;

    protected MenuGroupRequest() {
    }

    private MenuGroupRequest(String name) {
        this.name = name;
    }

    public static MenuGroupRequest of(String name) {
        return new MenuGroupRequest(name);
    }

    public String getName() {
        return name;
    }
}
