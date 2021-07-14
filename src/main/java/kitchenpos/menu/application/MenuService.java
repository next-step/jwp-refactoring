package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.event.MenuCreatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ApplicationEventPublisher publisher;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ApplicationEventPublisher publisher
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.publisher = publisher;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        MenuGroup menuGroup = findMenuGroupById(request.getMenuGroupId());
        Menu createdMenu = menuRepository.save(new Menu(request.getName(), request.getPrice(), menuGroup));
        publisher.publishEvent(new MenuCreatedEvent(createdMenu, request.getMenuProducts()));
        return MenuResponse.of(createdMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    public MenuGroup findMenuGroupById(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId).orElseThrow(IllegalArgumentException::new);
    }
}
