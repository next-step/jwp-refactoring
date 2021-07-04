package kitchenpos.application.query;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuGroupQueryService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupQueryService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }
}
