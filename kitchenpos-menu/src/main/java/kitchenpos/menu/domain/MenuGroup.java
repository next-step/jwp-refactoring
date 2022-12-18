package kitchenpos.menu.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    protected MenuGroup() {
    }

    private MenuGroup(MenuGroupBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
    }

    public static MenuGroupBuilder builder() {
        return new MenuGroupBuilder();
    }

    public static class MenuGroupBuilder {
        private Long id;
        private String name;

        public MenuGroupBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MenuGroupBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MenuGroup build() {
            return new MenuGroup(this);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
