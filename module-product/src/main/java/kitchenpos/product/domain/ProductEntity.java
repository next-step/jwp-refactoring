package kitchenpos.product.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Table(name = "product")
@Entity
public class ProductEntity {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Embedded
  private Price price;

  protected ProductEntity() {
  }

  public ProductEntity(String name, Double price) {
    this.name = name;
    this.price = Price.from(price);
  }

  public ProductEntity(Long id, String name, Double price) {
    this.id = id;
    this.name = name;
    this.price = Price.from(price);
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

  // 다른 aggregate에서 product aggregate에 접근할 때 Root entity인 ProductEntity에만 접근하고
  // 내부의 price는 노출시키지 않기 위해 BigDecimal 반환
  public BigDecimal calculateAmountWithQuantity(Long quantity) {
    return price.multiply(BigDecimal.valueOf(quantity))
            .getValue();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProductEntity that = (ProductEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(price, that.price);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, price);
  }
}
