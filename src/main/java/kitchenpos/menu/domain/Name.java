package kitchenpos.menu.domain;

import org.springframework.util.ObjectUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {

    @Column
    private String name;

    protected Name() {
    }

    private Name(String name) {
        if (ObjectUtils.isEmpty(name)) {
            throw new IllegalArgumentException("name은 null 또는 공백일 수 없습니다.");
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
