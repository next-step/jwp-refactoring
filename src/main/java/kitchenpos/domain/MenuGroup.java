package kitchenpos.domain;

public class MenuGroup {
    private Long id;
    private String name;

    public MenuGroup(){}

    public MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void updateName(final String name) {
        this.name = name;
    }
}
