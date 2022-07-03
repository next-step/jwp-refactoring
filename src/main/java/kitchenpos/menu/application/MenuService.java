package kitchenpos.menu.application;

import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Menus;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Products;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupService menuGroupService,
            final ProductService productService
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        MenuGroup menuGroup = menuGroupService.getMenuGroup(request.getMenuGroupId());
        Products products = productService.findMenusInIds(request.getProductIds());
        Menu menu = request.of(menuGroup, products);

        return MenuResponse.from(menuRepository.save(menu));
    }

    public List<MenuResponse> listResponse() {
        return new Menus(menuRepository.findAll()).toResponse();
    }

    public Menus findMenusInIds(final List<Long> ids) {
        return new Menus(menuRepository.findAllById(ids));
    }
}
