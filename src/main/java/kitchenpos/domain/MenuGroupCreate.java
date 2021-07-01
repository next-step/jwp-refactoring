package kitchenpos.domain;

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
