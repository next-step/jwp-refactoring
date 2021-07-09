package kitchenpos.menugroup.domain;

import javax.persistence.*;

@Table(name = "menu_group")
@Entity
public class MenuGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MenuGroupEntity() {
    }

    public MenuGroupEntity(String name) {
        this.name = name;
    }
}
