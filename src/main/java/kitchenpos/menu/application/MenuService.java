package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        validExistMenuGroup(menuRequest);
        final Menu menu = menuRepository.save(menuRequest.toMenu());

        return MenuResponse.of(menu);
    }

    private void validExistMenuGroup(MenuRequest menuRequest) {
        if (!menuGroupRepository.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    public List<MenuResponse> list() {
        return MenuResponse.listOf(menuRepository.findAll());
    }
}
