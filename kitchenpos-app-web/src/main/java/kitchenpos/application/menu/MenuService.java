package kitchenpos.application.menu;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.repository.menu.MenuRepository;
import kitchenpos.repository.menugroup.MenuGroupRepository;
import kitchenpos.repository.product.ProductRepository;
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
