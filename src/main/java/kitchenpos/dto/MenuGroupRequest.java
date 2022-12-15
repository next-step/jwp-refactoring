package kitchenpos.dto;

public class MenuGroupRequest {
    private String name;

    private MenuGroupRequest() {
    }

    private MenuGroupRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
