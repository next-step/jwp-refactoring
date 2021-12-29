package kitchenpos.menu.application;

import kitchenpos.common.exception.NotFoundEntityException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
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

    @Validated
    public MenuResponse create(@Valid MenuRequest menuRequest) {
        MenuGroup menuGroup = findMenuGroupById(menuRequest.getMenuGroupId());
        final Menu menu = menuRepository.save(menuRequest.toMenu(menuGroup));

        return MenuResponse.of(menu);
    }

    private MenuGroup findMenuGroupById(Long id) {
        return menuGroupRepository.findById(id)
                .orElseThrow(NotFoundEntityException::new);
    }

    public List<MenuResponse> list() {
        return MenuResponse.listOf(menuRepository.findAll());
    }
}
