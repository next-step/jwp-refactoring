package kitchenpos.menugroup.domain;

public class MenuGroup {
    private Long id;
    private String name;

    public MenuGroup() {
    }

    public MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroup of(MenuGroupEntity menuGroupEntity) {
        return new MenuGroup(menuGroupEntity.getId(), menuGroupEntity.getName());
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
