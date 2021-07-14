package kitchenpos.menu.domain;

import javax.persistence.*;
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

  @Column(name = "product_id", nullable = false)
  private Long productId;

  @Embedded
  private Quantity quantity;

  protected MenuProductEntity() {
  }

  public MenuProductEntity(Long productId, long quantity) {
    this.productId = productId;
    this.quantity = Quantity.from(quantity);
  }

  public MenuProductEntity(Long seq, Long productId, long quantity) {
    this.seq = seq;
    this.productId = productId;
    this.quantity = Quantity.from(quantity);
  }

  public Long getSeq() {
    return seq;
  }

  public MenuEntity getMenu() {
    return menu;
  }

  public Long getProductId() {
    return productId;
  }

  public long getQuantity() {
    return quantity.getValue();
  }

  public void defineParentMenu(MenuEntity menuEntity) {
    this.menu = menuEntity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MenuProductEntity that = (MenuProductEntity) o;
    return Objects.equals(seq, that.seq) && Objects.equals(menu, that.menu) && Objects.equals(productId, that.productId) && Objects.equals(quantity, that.quantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seq, menu, productId, quantity);
  }
}
