package kitchenpos.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {
    @Column(nullable = false)
    private String name;

    protected Name() {
    }

    public Name(String name) {
        validateName(name);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private void validateName(String name) {
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
