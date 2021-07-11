package kitchenpos.ui.dto.menu;

public class MenuGroupRequest {
    private Long id;
    private String name;

    protected MenuGroupRequest() {
    }

    private MenuGroupRequest(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupRequest of(String name) {
        return new MenuGroupRequest(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
