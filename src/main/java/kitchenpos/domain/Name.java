package kitchenpos.domain;

import org.springframework.util.ObjectUtils;

public class Name {

    private final String name;

    public Name(String name) {
        validateName(name);
        this.name = name;
    }

    public String value() {
        return name;
    }

    private void validateName(String name) {
        if (ObjectUtils.isEmpty(name)) {
            throw new IllegalArgumentException("이름은 공백이 들어올 수 없습니다.");
        }
    }

}
