package kitchenpos.product.dto;

import kitchenpos.product.domain.ProductEntity;

import java.util.Objects;

public class ProductRequest {

  private String name;
  private Double price;

  public ProductRequest(String name, Double price) {
    this.name = name;
    this.price = price;
  }

  public String getName() {
    return name;
  }

  public Double getPrice() {
    return price;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProductRequest that = (ProductRequest) o;
    return Objects.equals(name, that.name) && Objects.equals(price, that.price);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, price);
  }

  public ProductEntity toEntity() {
    return new ProductEntity(name, price);
  }
}
