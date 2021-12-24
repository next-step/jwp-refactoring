package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;
    private final ProductService productService;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuValidator menuValidator,
            final ProductService productService
    ) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        menuValidator.existMenuGroup(menuRequest.getMenuGroupId());
        final Menu menu = menuRequest.toMenu();
        final List<MenuProduct> menuProducts = productService.getMenuProducts(menuRequest.getMenuProducts());

        menu.addMenuProducts(menuProducts);

        final Menu savedMenu = menuRepository.save(menu);

        return MenuResponse.from(savedMenu);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

}
