package kitchenpos.menugroup.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    protected MenuGroup() {
    }

    public MenuGroup(String name) {
        this(null, name);
    }

    public MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
        validateNonNullFields();
    }

    private void validateNonNullFields() {
        if (name == null) {
            throw new IllegalArgumentException("이름은 메뉴 그룹의 필수 사항입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
