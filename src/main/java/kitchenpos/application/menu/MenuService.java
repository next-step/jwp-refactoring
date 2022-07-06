package kitchenpos.application.menu;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.dto.menu.CreateMenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    public static final String NOT_EXIST_MENU_GROUP_ERROR_MESSAGE = "메뉴 그룹 정보가 존재하지 않습니다.";

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductService menuProductService;

    public MenuService(
        MenuRepository menuRepository,
        MenuGroupRepository menuGroupRepository,
        MenuProductService menuProductService
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductService = menuProductService;
    }

    @Transactional
    public MenuResponse create(final CreateMenuRequest createMenuRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(createMenuRequest.getMenuGroupId())
            .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_MENU_GROUP_ERROR_MESSAGE));

        List<MenuProduct> menuProducts = menuProductService
            .findMenuProductByMenuProductRequest(createMenuRequest.getMenuProductRequests());

        Menu menu = createMenuRequest.toMenu(menuGroup, menuProducts);
        final Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.from(savedMenu);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }
}
