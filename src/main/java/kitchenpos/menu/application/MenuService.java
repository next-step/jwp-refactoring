package kitchenpos.menu.application;

import static kitchenpos.common.ErrorMessage.NOT_EXIST_MENU_GROUP;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.exception.NotExistException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Quantity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuValidator menuValidator;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                       MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> new NotExistException(NOT_EXIST_MENU_GROUP.message()));
        final Menu menu = new Menu.Builder(menuRequest.getName())
                .setPrice(Price.of(menuRequest.getPrice()))
                .setMenuGroup(menuGroup)
                .build();

        for (MenuProductRequest menuProduct : menuRequest.getMenuProducts()) {
            final Long productId = menuValidator.findProductId(menuProduct.getProductId());
            menu.addProduct(productId, Quantity.of(menuProduct.getQuantity()));
        }

        menuValidator.validateProductsTotalPrice(menu);
        final Menu persist = menuRepository.save(menu);
        return persist.toMenuResponse();
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(Menu::toMenuResponse)
                .collect(Collectors.toList());
    }
}
