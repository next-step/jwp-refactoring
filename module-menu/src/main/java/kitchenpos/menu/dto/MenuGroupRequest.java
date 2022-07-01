package kitchenpos.menu.dto;

public class MenuGroupRequest {
    private final Long id;
    private final String name;

    public MenuGroupRequest(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
