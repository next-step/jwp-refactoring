package kitchenpos.domain;

import kitchenpos.domain.common.Name;

public class MenuGroup {
    private Long id;
    private Name name;

    public MenuGroup() {
    }

    public MenuGroup(String name) {
        this.name = new Name(name);
    }

    public MenuGroup(Long id, String name) {
        this.id = id;
        this.name = new Name(name);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name.getValue();
    }

}
