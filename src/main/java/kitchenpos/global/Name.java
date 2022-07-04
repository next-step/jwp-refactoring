package kitchenpos.global;

import org.springframework.util.StringUtils;

import javax.persistence.Column;

public class Name {
    @Column(name="name", nullable = false)
    private String value;

    protected Name() {
    }

    public Name(String value) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException("잘못된 이름을 입력하였습니다.");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
