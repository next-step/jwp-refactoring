package kitchenpos.menu.domain;

import kitchenpos.product.domain.ProductEntity;

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

  @Column(name = "menu_group_id", nullable = false)
  private Long menuGroupId;

  @Embedded
  private MenuProducts menuProducts = new MenuProducts();

  protected MenuEntity() {
  }

  public MenuEntity(String name, Double menuPrice, Long menuGroupId, List<MenuProductEntity> products) {
    this(null, name, menuPrice, menuGroupId, products);
  }

  public MenuEntity(Long id, String name, Double menuPrice, Long menuGroupId, List<MenuProductEntity> products) {
    this.id = id;
    this.name = name;
    this.price = Price.fromDouble(menuPrice);
    this.menuGroupId = menuGroupId;
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

  public Long getMenuGroupId() {
    return menuGroupId;
  }

  public List<MenuProductEntity> getMenuProducts() {
    return menuProducts.getMenuProducts();
  }

  public List<Long> getMenuProductProductIds() {
    return menuProducts.getProductIds();
  }

  public void validatePrice(List<ProductEntity> products) {
    BigDecimal productsAmount = menuProducts.calculateAmount(products);
    if (price.compare(productsAmount) > 0) {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MenuEntity that = (MenuEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(menuGroupId, that.menuGroupId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, price, menuGroupId);
  }
}
