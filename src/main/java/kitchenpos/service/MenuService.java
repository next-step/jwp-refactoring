package kitchenpos.service;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {
    private final MenuGroupService menuGroupService;
    private final ProductService productService;
    private final MenuRepository menuRepository;

    public MenuService(final MenuGroupService menuGroupService, final ProductService productService, final MenuRepository menuRepository) {
        this.menuGroupService = menuGroupService;
        this.productService = productService;
        this.menuRepository = menuRepository;
    }

    public MenuResponse save(final MenuRequest menuRequest) {
        List<Long> ids = menuRequest.getMenuIds();
        Map<Long, Product> products = productService.findProducts(ids);

        MenuGroup menuGroup = menuGroupService.findMenuGroup(menuRequest.getMenuGroupId());

        Menu menu = menuRequest.toMenu(menuGroup);
        menuRequest.putMenuProduct(menu, products);
        return MenuResponse.of(menuRepository.save(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> findAll() {
        return MenuResponse.ofList(menuRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Map<Long, Menu> findMenus(List<Long> ids) {
        return menuRepository.findByIdIn(ids).stream().collect(Collectors.toMap(Menu::getId, Function.identity()));
    }
}
