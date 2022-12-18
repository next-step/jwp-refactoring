package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.springframework.util.StringUtils;

@Embeddable
public class MenuName {

    @Column(length = 255, nullable = false)
    private String name;

    protected MenuName() {
    }

    public MenuName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("메뉴의 이름은 반드시 입력되어야 합니다[name:" + name + "]");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
