package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        MenuGroup menuGroup = findMenuGroup(request.getMenuGroupId());
        MenuProducts menuProducts = findMenuProducts(request.getMenuProducts());
        Menu menu = request.toMenu(menuGroup, menuProducts);
        return MenuResponse.of(menuRepository.save(menu));
    }

    public List<MenuResponse> findAll() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::of)
                .collect(toList());
    }

    private MenuGroup findMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException("등록 되지 않은 메뉴 그룹 입니다."));
    }

    private MenuProducts findMenuProducts(List<MenuProductRequest> request) {
        return request
                .stream()
                .map(this::findMenuProduct)
                .collect(collectingAndThen(toList(), MenuProducts::new));
    }

    private MenuProduct findMenuProduct(MenuProductRequest menuProductRequest) {
        Product product = productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("등록 되지 않은 상품 입니다."));
        return new MenuProduct(product, menuProductRequest.getQuantity());
    }
}
