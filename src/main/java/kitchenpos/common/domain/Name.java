package kitchenpos.common.domain;

import static java.util.Objects.requireNonNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {

    @Column(name = "name", nullable = false)
    private String name;

    // JPA 기본 생성자 이므로 사용 금지
    protected Name() {
    }

    public Name(String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        requireNonNull(name, "이름이 존재하지 않습니다.");

        if (name.isEmpty()) {
            throw new IllegalArgumentException("이름이 존재하지 않습니다.");
        }
    }

    public String getName() {
        return name;
    }

}
