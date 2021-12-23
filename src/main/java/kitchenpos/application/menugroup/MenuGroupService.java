package kitchenpos.application.menugroup;

import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.dto.menugroup.MenuGroupRequest;
import kitchenpos.repository.menugroup.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroup create(final MenuGroupRequest menuGroupRequest) {
        return menuGroupRepository.save(menuGroupRequest.toEntity());
    }

    @Transactional(readOnly=true)
    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }
}
