package kitchenpos.menu.domain;

import kitchenpos.product.domain.ProductEntity;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class MenuProducts {

  @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<MenuProductEntity> menuProductEntities = new HashSet<>();

  protected MenuProducts() {

  }

  public void addMenuProducts(MenuEntity menuEntity, List<MenuProductEntity> products) {
    products.forEach(product -> product.defineParentMenu(menuEntity));
    menuProductEntities.addAll(products);
  }

  public List<MenuProductEntity> getMenuProducts() {
    return new ArrayList<>(menuProductEntities);
  }

  public List<Long> getProductIds() {
    return menuProductEntities.stream()
            .map(MenuProductEntity::getProductId)
            .collect(Collectors.toList());
  }

  public BigDecimal calculateAmount(List<ProductEntity> products) {
    Map<Long, Long> productPerQuantity = makeProductPerQuantity();
    return products.stream()
        .map(productEntity -> productEntity.calculateAmountWithQuantity(productPerQuantity.getOrDefault(productEntity.getId(), 0L)))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private Map<Long, Long> makeProductPerQuantity() {
    return menuProductEntities.stream()
        .collect(Collectors.toMap(MenuProductEntity::getProductId, MenuProductEntity::getQuantity));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MenuProducts that = (MenuProducts) o;
    return Objects.equals(menuProductEntities, that.menuProductEntities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(menuProductEntities);
  }
}
