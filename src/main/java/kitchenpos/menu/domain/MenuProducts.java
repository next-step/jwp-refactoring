package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

import static kitchenpos.menu.domain.Price.REDUCE_IDENTITY;

@Embeddable
public class MenuProducts {

  @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<MenuProductEntity> menuProductEntities = new HashSet<>();

  protected MenuProducts() {

  }

  public void addMenuProducts(MenuEntity menuEntity, List<MenuProductEntity> products) {
    products.forEach(product -> product.defineParentMenu(menuEntity));
    validateMenuProductsAmount(menuEntity, calculateMenuProductsAmount(products));
    menuProductEntities.addAll(products);
  }

  public List<MenuProductEntity> getMenuProducts() {
    return new ArrayList<>(menuProductEntities);
  }

  private void validateMenuProductsAmount(MenuEntity menuEntity, Price menuProductsAmount) {
    Price menuPrice = menuEntity.getPrice();
    if (menuPrice.compareTo(menuProductsAmount) > 0) {
      throw new IllegalArgumentException();
    }
  }

  private Price calculateMenuProductsAmount(List<MenuProductEntity> products) {
    return products.stream()
          .map(MenuProductEntity::calculateAmount)
          .reduce(REDUCE_IDENTITY, Price::sum);
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
