package kitchenpos.menu.domain;

public class MenuGroup {
    private Long id;
    private String name;

    public MenuGroup(Long id, String name) {
        this(name);
        this.id = id;
    }

    public MenuGroup(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
