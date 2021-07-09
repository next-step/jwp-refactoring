package kitchenpos.menugroup.domain;

import javax.persistence.*;

@Table(name = "menu_group")
@Entity
public class MenuGroupEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  protected MenuGroupEntity() {
  }

  public MenuGroupEntity(String name) {
    this.name = name;
  }

  public MenuGroupEntity(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
