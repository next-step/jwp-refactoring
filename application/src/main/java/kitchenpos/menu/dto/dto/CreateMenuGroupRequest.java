package kitchenpos.menu.dto.dto;

public class CreateMenuGroupRequest {

    private String name;

    protected CreateMenuGroupRequest() { }

    public CreateMenuGroupRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
