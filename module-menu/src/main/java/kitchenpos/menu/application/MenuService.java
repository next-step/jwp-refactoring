package kitchenpos.menu.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.common.exception.CannotCreateException;
import kitchenpos.common.exception.ExceptionType;
import kitchenpos.menu.application.validator.MenuValidatorGroup;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.request.MenuRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidatorGroup menuValidatorGroup;

    public MenuService(final MenuRepository menuRepository, final MenuValidatorGroup menuValidatorGroup) {
        this.menuRepository = menuRepository;
        this.menuValidatorGroup = menuValidatorGroup;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        menuValidatorGroup.validate(menuRequest);
        Menu menu = menuRequest.toEntity();
        Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.of(savedMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAllMenuAndProducts();

        return menus.stream()
            .map(MenuResponse::of)
            .collect(toList());
    }

    public Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new CannotCreateException(ExceptionType.NOT_EXIST_MENU.getMessage(menuId)));
    }

    public long countByIdIn(List<Long> menuIds) {
        return menuRepository.countByIdIn(menuIds);
    }
}
