package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuEntity;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.ProductEntity;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class MenuService2 {

  private final MenuRepository menuRepository;
  private final MenuGroupRepository menuGroupRepository;
  private final ProductRepository productRepository;

  public MenuService2(MenuRepository menuRepository,
                      MenuGroupRepository menuGroupRepository,
                      ProductRepository productRepository) {
    this.menuRepository = menuRepository;
    this.menuGroupRepository = menuGroupRepository;
    this.productRepository = productRepository;
  }

  public MenuResponse create(final MenuRequest request) {
    validateExistMenuGroup(request.getMenuGroupId());
    MenuEntity newMenuEntity = toEntity(request);
    validateMenuProductsAmount(newMenuEntity);
    return MenuResponse.from(menuRepository.save(newMenuEntity));
  }

  private void validateMenuProductsAmount(MenuEntity newMenuEntity) {
    List<ProductEntity> products = productRepository.findAllById(newMenuEntity.getMenuProductProductIds());
    newMenuEntity.validatePrice(products);
  }

  private MenuEntity toEntity(MenuRequest request) {
    return new MenuEntity(request.getName(), request.getPrice(), request.getMenuGroupId(), request.getProductEntities());
  }

  private void validateExistMenuGroup(Long menuGroupId) {
    if (!menuGroupRepository.existsById(menuGroupId)) {
     throw new IllegalArgumentException();
    }
  }

  @Transactional(readOnly = true)
  public List<MenuResponse> findAllMenus() {
    return MenuResponse.ofList(menuRepository.findAll());
  }
}
