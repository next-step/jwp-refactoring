package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.event.product.MenuCreatedEvent;
import kitchenpos.exception.MenuGroupAlreadyExistsException;
import kitchenpos.repository.MenuRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ApplicationEventPublisher eventPublisher;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupService menuGroupService,
            final ApplicationEventPublisher eventPublisher) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {

        Long menuGroupId = menuRequest.getMenuGroupId();

        if (menuGroupService.isExists(menuGroupId)) {
            throw new MenuGroupAlreadyExistsException("menuGroup: " + menuGroupId);
        }

        MenuGroup findMenuGroup = menuGroupService.findById(menuGroupId);

        Menu menu = Menu.of(menuRequest.getName(), menuRequest.getPrice(), findMenuGroup);

        List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProductRequests();

        menuProductRequests.stream()
                .map(menuProductRequest ->
                        MenuProduct.of(null,
                                menuProductRequest.getProductId(),
                                menuProductRequest.getQuantity()))
                .forEach(menu::addMenuProduct);

        eventPublisher.publishEvent(new MenuCreatedEvent(menu));

        return menuRepository.save(menu);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
