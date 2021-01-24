package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.product.application.ProductService;
import kitchenpos.menu.domain.Menu;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuGroupService menuGroupService;
    private final ProductService productService;
    private final MenuRepository menuRepository;

    public MenuService(
            final MenuGroupService menuGroupService,
            final ProductService productService,
            final MenuRepository menuRepository
    ) {
        this.menuGroupService = menuGroupService;
        this.productService = productService;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        Menu menu = menuRequest.toMenu();

        checkExistsMenuGroupAndProducts(menu);

        menu.addMenuToMenuProducts();

        return MenuResponse.of(menuRepository.save(menu));
    }

    private void checkExistsMenuGroupAndProducts(Menu menu) {
        menuGroupService.checkExistsMenuGroup(menu.getMenuGroup());
        productService.checkExistsProducts(menu.getProducts());
    }

    public List<MenuResponse> list() {
        menuRepository.findAll().forEach(menu -> System.out.println(menu.getId()));
        return menuRepository.findAll().stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }
}
