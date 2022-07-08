package kitchenpos.menu.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuValidator menuValidator;


    public MenuService(
            final MenuGroupRepository menuGroupRepository,
            final MenuRepository menuRepository,
            final MenuValidator menuValidator
    ) {
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.getOne(menuRequest.getMenuGroupId());

        menuValidator.existMenuGroup(menuGroup);
        menuValidator.validateMenuProduct(menuRequest);

        final Menu savedMenu = menuRepository.save(Menu.createMenu(menuRequest, menuGroup, fromMenuProducts(menuRequest)));
        return MenuResponse.from(savedMenu);
    }


    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MenuResponse findByMenuId(Long menuId) {
        final Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NoSuchElementException("해당 메뉴가 존재하지 않습니다"));

        return MenuResponse.from(menu);
    }

    private MenuProducts fromMenuProducts(MenuRequest menuRequest) {
        return MenuProducts.from(menuRequest
                .getMenuProducts()
                .stream()
                .map((productRequest) -> MenuProduct.of(productRequest.getProductId(), productRequest.getQuantity()))
                .collect(Collectors.toList()));
    }
}
