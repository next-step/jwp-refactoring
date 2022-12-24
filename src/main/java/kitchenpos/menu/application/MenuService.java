package kitchenpos.menu.application;

import kitchenpos.common.exception.NoSuchDataException;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.MenuProductService;
import kitchenpos.product.dto.MenuProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

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
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = findMenuGroupById(menuRequest.getMenuGroupId());
        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        final Menu savedMenu = menuRepository.save(menu);

        List<MenuProductResponse> menuProductResponses =
                menuProductService.createAll(menuRequest.getMenuProductRequests(), savedMenu.getId(), menuRequest.getPrice());

        return MenuResponse.of(savedMenu, menuProductResponses);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(menu ->
                        MenuResponse.of(menu, menuProductService.findMenuProductsByMenuId(menu.getId())))
                .collect(Collectors.toList());
    }

    private MenuGroup findMenuGroupById(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId).orElseThrow(NoSuchDataException::new);
    }
}