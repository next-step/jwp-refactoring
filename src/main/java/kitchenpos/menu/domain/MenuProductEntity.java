package kitchenpos.menu.domain;

import kitchenpos.product.domain.ProductEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Table(name = "menu_product")
@Entity
public class MenuProductEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long seq;

  @ManyToOne
  @JoinColumn(name = "menu_id", nullable = false)
  private MenuEntity menu;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private ProductEntity product;

  @Embedded
  private Quantity quantity;

  protected MenuProductEntity() {
  }

  public MenuProductEntity(ProductEntity product, Quantity quantity) {
    this.product = product;
    this.quantity = quantity;
  }

  public void defineParentMenu(MenuEntity menuEntity) {
    this.menu = menuEntity;
  }

  public Price calculateAmount() {
    BigDecimal productPrice = product.getPrice();
    return Price.from(productPrice.multiply(quantity.getBigDecimalValue()));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MenuProductEntity that = (MenuProductEntity) o;
    return Objects.equals(seq, that.seq) && Objects.equals(menu, that.menu) && Objects.equals(product, that.product) && Objects.equals(quantity, that.quantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seq, menu, product, quantity);
  }
}
