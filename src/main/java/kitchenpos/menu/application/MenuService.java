package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;
    private final MenuGroupService menuGroupService;

    public MenuService(MenuRepository menuRepository, ProductRepository productRepository, MenuGroupService menuGroupService) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
        this.menuGroupService = menuGroupService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        validateMenuGroup(request);
        Menu savedMenu = menuRepository.save(request.toMenu());
        savedMenu.addMenuProduct(toMenuProducts(request));
        return MenuResponse.from(savedMenu);
    }

    private void validateMenuGroup(MenuRequest request) {
        if (!menuGroupService.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private List<MenuProduct> toMenuProducts(MenuRequest request) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : request.getMenuProducts()) {
            Product product = productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. id=" + menuProductRequest.getProductId()));
            menuProducts.add(menuProductRequest.toMenuProduct(product));
        }
        return menuProducts;
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }
}
