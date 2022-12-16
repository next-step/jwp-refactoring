package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuValidator menuValidator;
    private final ProductRepository productRepository;

    public MenuService(
        final MenuRepository menuRepository,
        final MenuGroupRepository menuGroupRepository,
        final MenuValidator menuValidator,
        final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuValidator = menuValidator;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        List<Product> products = productRepository.findAllById(menuRequest.getProductIds());
        boolean menuGroupNotExists = !menuGroupRepository.existsById(menuRequest.getMenuGroupId());

        Menu savedMenu = menuRepository.save(createMenu(menuRequest));
        menuValidator.validate(savedMenu, products, menuGroupNotExists);

        return MenuResponse.from(savedMenu);
    }

    private Menu createMenu(MenuRequest menuRequest) {
        return Menu.generate(
            menuRequest.getName(),
            menuRequest.getPrice(),
            menuRequest.getMenuGroupId(),
            menuRequest.getMenuProducts().stream()
                .map(menuProduct -> MenuProduct.generate(
                    menuProduct.getProductId(),
                    menuProduct.getQuantity()
                )).collect(Collectors.toList())
        );
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }
}
