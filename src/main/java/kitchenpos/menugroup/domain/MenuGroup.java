package kitchenpos.menugroup.domain;

import javax.persistence.*;

import static java.util.Objects.isNull;

@Table(name = "menu_group")
@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    protected MenuGroup() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroup(String name) {
        validCheck(name);
        this.name = name;
    }

    private void validCheck(String name) {
        if (isNull(name)) {
            throw new IllegalArgumentException("메뉴그룹 이름은 필수로 입력되어야 합니다.");
        }
    }
}
