package kitchenpos.domain;


import org.springframework.util.StringUtils;

import javax.persistence.*;

@Entity
public class MenuGroup {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    protected MenuGroup() {
    }

    public MenuGroup(final String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("메뉴 이름은 필수 정보입니다.");
        }
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
