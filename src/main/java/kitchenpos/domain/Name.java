package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {
    @Column(name = "name", nullable = false)
    private final String name;

    private Name(String name) {
        this.name = name;
    }

    protected Name() {
        name = null;
    }

    public static Name of(String name) {
        return new Name(name);
    }

    public String getName() {
        return name;
    }
}
