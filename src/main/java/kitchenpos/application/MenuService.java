package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository,
            final MenuRepository menuRepository
    ) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.getOne(menuRequest.getMenuGroupId());

        final MenuProducts menuProducts = fromMenuProducts(menuRequest);

        final Menu savedMenu = menuRepository.save(Menu.createMenu(menuRequest, menuGroup, menuProducts));

        return MenuResponse.from(savedMenu);
    }

    private MenuProducts fromMenuProducts(MenuRequest menuRequest) {
        return MenuProducts.from(menuRequest
                .getMenuProducts()
                .stream()
                .map((productRequest) -> MenuProduct.of(productRepository.getOne(productRequest.getProductId()),
                        productRequest.getQuantity()))
                .collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
