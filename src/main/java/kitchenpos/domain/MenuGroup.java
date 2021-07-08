package kitchenpos.domain;

import static java.util.Objects.*;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import kitchenpos.product.domain.Name;

@Entity
@Table
public class MenuGroup {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @AttributeOverride(name = "value", column = @Column(name = "name", nullable = false))
    private Name name;

    protected MenuGroup() {}

    public MenuGroup(Name name) {
        validateNonNull(name);
        this.name = name;
    }

    private void validateNonNull(Name name) {
        if (isNull(name)) {
            throw new IllegalArgumentException("메뉴 그룹의 이름은 필수 정보 입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }
}
