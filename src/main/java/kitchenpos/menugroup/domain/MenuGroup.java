package kitchenpos.menugroup.domain;

import io.micrometer.core.instrument.util.StringUtils;

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
        if (StringUtils.isBlank(name)) {
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
