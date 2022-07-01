package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.validator.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuValidator menuValidator
    ) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        menuValidator.validate(menuRequest);
        return MenuResponse.from(menuRepository.save(menuRequest.toMenu()));
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAllWithFetchJoin();
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    public int countByIdIn(List<Long> menuIds) {
        return menuRepository.countByIdIn(menuIds);
    }

    public Menu findMenu(long menuId) {
        return menuRepository.findById(menuId).orElseThrow(() -> new IllegalArgumentException("메뉴를 조회할 수 없습니다."));
    }
}
