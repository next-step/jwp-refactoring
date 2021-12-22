package kitchenpos.domain;

public class MenuGroup {
    private Long id;
    private String name;

    public MenuGroup() {
    }

    private MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroup of(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
