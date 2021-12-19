package kitchenpos.api.application.menu;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.domain.menu.Menu;
import kitchenpos.common.domain.menu.MenuProduct;
import kitchenpos.common.domain.menu.MenuRepository;
import kitchenpos.common.domain.menu.MenuValidator;
import kitchenpos.common.domain.menugroup.MenuGroup;
import kitchenpos.common.domain.menugroup.MenuGroupRepository;
import kitchenpos.common.dto.menu.MenuProductRequest;
import kitchenpos.common.dto.menu.MenuRequest;
import kitchenpos.common.dto.menu.MenuResponse;
import kitchenpos.common.utils.StreamUtils;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = findMenuGroup(menuRequest.getMenuGroupId());
        List<MenuProduct> menuProducts = StreamUtils.mapToList(menuRequest.getMenuProducts(),
                                                               MenuProductRequest::toMenuProduct);
        Menu menu = Menu.of(menuRequest.getName(), menuRequest.getPrice(), menuGroup.getId(), menuProducts);

        menuValidator.validate(menu);

        return MenuResponse.from(menuRepository.save(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return StreamUtils.mapToList(menus, MenuResponse::from);
    }

    private MenuGroup findMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                                  .orElseThrow(EntityNotFoundException::new);
    }
}
