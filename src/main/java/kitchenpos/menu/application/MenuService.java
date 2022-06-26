package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
        ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        validateCreateRequest(menuRequest);

        if (!menuGroupRepository.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }

        Menu menu = new Menu(
            menuRequest.getName(),
            menuRequest.getPrice(),
            menuRequest.getMenuGroupId(),
            convertMenuProductEntity(menuRequest.getMenuProducts())
        );

        menuRepository.save(menu);

        return MenuResponse.of(menu);
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();

        return menus.stream()
            .map(MenuResponse::of)
            .collect(Collectors.toList());
    }

    private List<MenuProduct> convertMenuProductEntity(List<MenuProductRequest> menuProductRequests) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            Product product = productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(IllegalArgumentException::new);
            menuProducts.add(new MenuProduct(product, menuProductRequest.getQuantity()));
        }
        return menuProducts;
    }

    private void validateCreateRequest(MenuRequest menuRequest) {
        if (menuRequest.getMenuProducts() == null) {
            throw new IllegalArgumentException("상품이 설정되지 않았습니다.");
        }

        if (menuRequest.getMenuGroupId() == null) {
            throw new IllegalArgumentException("메뉴 그룹이 설정되지 않았습니다.");
        }
    }
}
