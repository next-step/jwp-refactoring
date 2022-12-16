package kitchenpos.menu.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.common.domain.Name;

@Entity
@Table(name = "menu_group")
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    protected MenuGroup() {}

    public MenuGroup(Long id, String name) {
        this.id = id;
        this.name = Name.from(name);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public String getNameValue() {
        return name.value();
    }

    public static class Builder {

        private Long id;
        private String name;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public MenuGroup build() {
            return new MenuGroup(id, name);
        }
    }
}
