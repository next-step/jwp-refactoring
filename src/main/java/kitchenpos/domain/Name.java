package kitchenpos.domain;

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
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("이름이 존재하지 않습니다.");
        }

        this.name = name;
    }

    public String getName() {
        return name;
    }

}
