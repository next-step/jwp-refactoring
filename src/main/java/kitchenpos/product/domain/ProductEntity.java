package kitchenpos.product.domain;

import javax.persistence.*;
import java.math.BigDecimal;

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
}
