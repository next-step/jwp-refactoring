package kitchenpos.product.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {
    @Column
    private String name;

    protected Name() {
    }

    private Name(String name) {
        if (null == name) {
            throw new IllegalArgumentException("이름이 존재해야합니다.");
        }
        this.name = name;
    }

    public static Name of(String name) {
        return new Name(name);
    }

    public String getName() {
        return name;
    }
}
