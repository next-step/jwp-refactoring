package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGeneratedEvent;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Transactional(readOnly = true)
@Service
public class MenuService {
    public static final String NOT_EXIST_MENU_GROUP = "존재하지 않는 메뉴그룹";
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public MenuResponse create(MenuRequest menuRequest) {
        MenuGroup menuGroup = getMenuGroup(menuRequest);
        Menu menu = Menu.of(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        menuRequest.registerMenu(menu);
        applicationEventPublisher.publishEvent(new MenuGeneratedEvent(menu));

        Menu savedMenu = menuRepository.save(menu);
        return new MenuResponse(savedMenu);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::new)
                .collect(Collectors.toList());
    }

    private MenuGroup getMenuGroup(MenuRequest menuRequest) {
        return menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> new MenuGroupNotFoundException(NOT_EXIST_MENU_GROUP));
    }
}
