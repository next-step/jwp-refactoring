package kitchenpos.product.domain;

import javax.persistence.*;

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

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Price getPrice() {
    return price;
  }
}
