package kitchenpos.service;


import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuRequest.MenuProductRequest;
import kitchenpos.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class MenuService {

    private final MenuRepository menuRepository;

    private final MenuGroupRepository menuGroupRepository;

    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = toMenuGroup(menuRequest.getMenuGroupId());
        List<MenuProduct> menuProducts = toMenuProducts(menuRequest.getMenuProducts());
        Menu savedMenu = menuRepository.save(menuRequest.toMenu(menuGroup, menuProducts));
        return MenuResponse.of(savedMenu);
    }

    private MenuGroup toMenuGroup(final Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("%d에 해당하는 메뉴 그룹이 없습니다.", menuGroupId)));
    }

    private List<MenuProduct> toMenuProducts(final List<MenuProductRequest> menuProducts) {
        return menuProducts.stream()
                .map(this::toMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct toMenuProduct(final MenuProductRequest menuProductRequest) {
        Long productId = menuProductRequest.getProductId();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("%d에 해당하는 상품이 없습니다.", productId)));
        return new MenuProduct(product, menuProductRequest.getQuantity());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> findAll() {
        List<Menu> menus = menuRepository.findAll();
        return MenuResponse.ofList(menus);
    }
}
