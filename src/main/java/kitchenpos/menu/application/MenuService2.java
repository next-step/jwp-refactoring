package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuEntity;
import kitchenpos.menu.domain.MenuProductEntity;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroupEntity;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

  public MenuResponse create(final MenuRequest menu) {
    return MenuResponse.from(menuRepository.save(toEntity(menu)));
  }

  private MenuEntity toEntity(MenuRequest request) {
    return new MenuEntity(request.getName(), request.getPrice(), findMenuGroup(request.getMenuGroupId()), toMenuProductEntity(request));
  }

  private MenuGroupEntity findMenuGroup(Long menuGroupId) {
    return menuGroupRepository.findById(menuGroupId)
        .orElseThrow(IllegalArgumentException::new);
  }

  private List<MenuProductEntity> toMenuProductEntity(MenuRequest request) {
    return productRepository.findAllById(request.getProductIds())
            .stream()
            .map(productEntity -> new MenuProductEntity(productEntity, findProductQuantity(request.getMenuProducts(), productEntity.getId())))
            .collect(Collectors.toList());
  }

  private Long findProductQuantity(List<MenuRequest.MenuProductRequest> menuProducts, Long productId) {
    return menuProducts.stream()
        .filter(menuProductRequest -> productId.equals(menuProductRequest.getProductId()))
        .findFirst()
        .map(MenuRequest.MenuProductRequest::getQuantity)
        .orElseThrow(IllegalArgumentException::new);
  }

  @Transactional(readOnly = true)
  public List<MenuResponse> findAllMenus() {
    return MenuResponse.ofList(menuRepository.findAll());
  }
}
