package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuEntity;
import kitchenpos.menu.domain.MenuProductEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MenuResponse {

  private final long id;
  private final String name;
  private final BigDecimal price;
  private final long menuGroupId;
  private final List<MenuProductResponse> menuProducts;

  public MenuResponse(long id, String name, BigDecimal price, long menuGroupId, List<MenuProductResponse> menuProducts) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.menuGroupId = menuGroupId;
    this.menuProducts = menuProducts;
  }

  public static MenuResponse from(MenuEntity entity) {
    return new MenuResponse(entity.getId(),
        entity.getName(),
        entity.getPrice(),
        entity.getMenuGroupId(),
        toMenuProductResponse(entity.getMenuProducts()));
  }

  private static List<MenuProductResponse> toMenuProductResponse(List<MenuProductEntity> menuProducts) {
    return menuProducts.stream()
            .map(MenuProductResponse::from)
            .collect(Collectors.toList());
  }

  public static List<MenuResponse> ofList(List<MenuEntity> menus) {
    return menus.stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public long getMenuGroupId() {
    return menuGroupId;
  }

  public List<MenuProductResponse> getMenuProducts() {
    return menuProducts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MenuResponse that = (MenuResponse) o;
    return id == that.id && menuGroupId == that.menuGroupId && Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(menuProducts, that.menuProducts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, price, menuGroupId, menuProducts);
  }

  public static class MenuProductResponse {

    private final long seq;
    private final long menuId;
    private final long productId;
    private final long quantity;

    public MenuProductResponse(Long seq, Long menuId, Long productId, long quantity) {
      this.seq = seq;
      this.menuId = menuId;
      this.productId = productId;
      this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProductEntity entity) {
      return new MenuProductResponse(entity.getSeq(), entity.getMenu().getId(), entity.getProductId(), entity.getQuantity());
    }

    public long getSeq() {
      return seq;
    }

    public long getMenuId() {
      return menuId;
    }

    public long getProductId() {
      return productId;
    }

    public long getQuantity() {
      return quantity;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      MenuProductResponse that = (MenuProductResponse) o;
      return seq == that.seq && menuId == that.menuId && productId == that.productId && quantity == that.quantity;
    }

    @Override
    public int hashCode() {
      return Objects.hash(seq, menuId, productId, quantity);
    }
  }
}
