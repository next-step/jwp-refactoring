package kitchenpos.commonDomain;

import org.springframework.util.StringUtils;

import javax.persistence.Column;

public class Name {
    @Column(nullable = false)
    private String name;

    protected Name() {
    }

    public Name(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("잘못된 이름을 입력하였습니다.");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
