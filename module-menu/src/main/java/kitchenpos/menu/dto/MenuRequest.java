package kitchenpos.menu.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.menu.domain.MenuProductEntity;

import java.util.List;
import java.util.stream.Collectors;

public class MenuRequest {

  private final String name;
  private final Double price;
  private final Long menuGroupId;
  private final List<MenuProductRequest> menuProducts;

  @JsonCreator
  public MenuRequest(@JsonProperty("name") String name,
                     @JsonProperty("price") Double price,
                     @JsonProperty("menuGroupId") Long menuGroupId,
                     @JsonProperty("menuProducts") List<MenuProductRequest> menuProducts) {
    this.name = name;
    this.price = price;
    this.menuGroupId = menuGroupId;
    this.menuProducts = menuProducts;
  }

  public String getName() {
    return name;
  }

  public Double getPrice() {
    return price;
  }

  public Long getMenuGroupId() {
    return menuGroupId;
  }

  public List<MenuProductRequest> getMenuProducts() {
    return menuProducts;
  }

  public List<MenuProductEntity> getProductEntities() {
    return menuProducts.stream()
            .map(MenuProductRequest::toEntity)
            .collect(Collectors.toList());
  }

  public static class MenuProductRequest {
    private final Long productId;
    private final Long quantity;

    @JsonCreator
    public MenuProductRequest(@JsonProperty("productId") Long productId,
                              @JsonProperty("quantity") Long quantity) {
      this.productId = productId;
      this.quantity = quantity;
    }

    public Long getProductId() {
      return productId;
    }

    public Long getQuantity() {
      return quantity;
    }

    public MenuProductEntity toEntity() {
      return new MenuProductEntity(productId, quantity);
    }
  }
}
