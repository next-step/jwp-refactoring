package kitchenpos.menu.domain;

public class MenuGroup {
    private Long id;
    private String name;

    private MenuGroup() {}

    public MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroup of(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
