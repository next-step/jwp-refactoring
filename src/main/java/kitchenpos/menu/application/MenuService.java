package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse createMenu(final MenuCreateRequest request) {
        Menu menu = mapToMenu(request);
        return MenuResponse.of(menuRepository.save(menu));
    }

    public List<MenuResponse> findAllMenus() {
        return MenuResponse.list(menuRepository.findAllWithGroupAndProducts());
    }

    private Menu mapToMenu(MenuCreateRequest request) {
        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(EntityNotFoundException::new);
        MenuProducts menuProducts = mapToMenuProducts(request.getMenuProducts());
        return new Menu(
                request.getName(),
                Price.of(request.getPrice()),
                menuGroup,
                menuProducts
        );
    }

    private MenuProducts mapToMenuProducts(List<MenuProductRequest> menuProductRequests) {
        List<MenuProduct> menuProducts = menuProductRequests.stream()
                .map(this::mapToMenuProduct)
                .collect(Collectors.toList());
        return new MenuProducts(menuProducts);
    }

    private MenuProduct mapToMenuProduct(MenuProductRequest menuProductRequest) {
        Product product = productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(EntityNotFoundException::new);
        return MenuProduct.of(product, menuProductRequest.getQuantity());
    }
}
