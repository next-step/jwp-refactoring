package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.event.MenuCreatedEvent;
import kitchenpos.menuproduct.domain.MenuProductRepository;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.ui.exception.NoMenuGroupException;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ApplicationEventPublisher publisher;

    public MenuService(
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository, ApplicationEventPublisher publisher) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.publisher = publisher;
    }

    public MenuResponse create(MenuRequest menuRequest) {
        Menu menu = Menu.of(menuRequest);

        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new NoMenuGroupException();
        }

        final Menu savedMenu = menuRepository.save(menu);

        publisher.publishEvent(new MenuCreatedEvent(savedMenu, menuRequest.getMenuProducts()));

        return MenuResponse.of(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream().map(MenuResponse::of).collect(Collectors.toList());
    }
}
