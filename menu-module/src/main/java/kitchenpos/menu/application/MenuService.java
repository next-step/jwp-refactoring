package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.publisher.MenuEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuEventPublisher eventPublisher;

    public MenuService(MenuRepository menuRepository, MenuEventPublisher eventPublisher) {
        this.menuRepository = menuRepository;
        this.eventPublisher = eventPublisher;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        eventPublisher.createMenuValidPublishEvent(menuRequest);
        Menu menu = menuRequest.toMenu();
        menu.addMenuProducts(menuRequest.toMenuProducts());
        return MenuResponse.of(menuRepository.save(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream().map(MenuResponse::of).collect(Collectors.toList());
    }

    public long countByMenuId(List<Long> menuIds) {
        return menuRepository.countByIdIn(menuIds);
    }

}
