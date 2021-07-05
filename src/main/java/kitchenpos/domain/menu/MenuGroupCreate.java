package kitchenpos.domain.menu;

import kitchenpos.domain.Name;

public class MenuGroupCreate {
    private Name name;

    public MenuGroupCreate(String name) {
        this(new Name(name));
    }

    public MenuGroupCreate(Name name) {
        this.name = name;
    }

    public Name getName() {
        return name;
    }
}
