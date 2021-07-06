package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroupEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Table(name = "menu")
@Entity
public class MenuEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Embedded
  private Price price;

  @ManyToOne
  @JoinColumn(name = "menu_group_id", nullable = false)
  private MenuGroupEntity menuGroup;

  @Embedded
  private MenuProducts menuProducts = new MenuProducts();

  protected MenuEntity() {
  }

  public MenuEntity(String name, Double menuPrice, MenuGroupEntity menuGroup, List<MenuProductEntity> products) {
    this.name = name;
    this.price = Price.from(menuPrice);
    this.menuGroup = menuGroup;
    menuProducts.addMenuProducts(this, products);
  }

  public MenuEntity(Long id, String name, Double menuPrice, MenuGroupEntity menuGroup, List<MenuProductEntity> products) {
    this.id = id;
    this.name = name;
    this.price = Price.from(menuPrice);
    this.menuGroup = menuGroup;
    menuProducts.addMenuProducts(this, products);
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getPrice() {
    return price.getValue();
  }

  public MenuGroupEntity getMenuGroup() {
    return menuGroup;
  }

  public List<MenuProductEntity> getMenuProducts() {
    return menuProducts.getMenuProducts();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MenuEntity that = (MenuEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(menuGroup, that.menuGroup);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, price, menuGroup);
  }
}
