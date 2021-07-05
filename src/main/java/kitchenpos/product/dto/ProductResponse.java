package kitchenpos.product.dto;

import kitchenpos.product.domain.ProductEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProductResponse {

  private final Long id;
  private final String name;
  private final double price;

  public ProductResponse(Long id, String name, double price) {
    this.id = id;
    this.name = name;
    this.price = price;
  }

  public static ProductResponse from(ProductEntity product) {
    return new ProductResponse(product.getId(), product.getName(), product.getPrice().doubleValue());
  }

  public static List<ProductResponse> ofList(List<ProductEntity> products) {
    return products.stream()
          .map(ProductResponse::from)
          .collect(Collectors.toList());
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public double getPrice() {
    return price;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProductResponse that = (ProductResponse) o;
    return Double.compare(that.price, price) == 0 && Objects.equals(id, that.id) && Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, price);
  }
}
