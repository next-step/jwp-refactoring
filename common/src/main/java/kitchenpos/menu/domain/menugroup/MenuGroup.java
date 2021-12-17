package kitchenpos.menu.domain.menugroup;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static org.springframework.util.ObjectUtils.isEmpty;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    protected MenuGroup() {
    }

    public MenuGroup(String name) {
        validation(name);
        this.name = name;
    }

    private void validation(String name) {
        if (isEmpty(name)) {
            throw new IllegalArgumentException("메뉴 그룹명이 없습니다");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
