package kitchenpos.application;

import kitchenpos.advice.exception.MenuGroupException;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroup create(final MenuGroupRequest menuGroupRequest) {
        return menuGroupRepository.save(menuGroupRequest.toMenuGroup());
    }

    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }

    public MenuGroup findById(Long id) {
        return menuGroupRepository.findById(id).orElseThrow(() -> new MenuGroupException("존재하는 메뉴그룹이 없습니다", id));
    }

    public void validateExistsMenuGroup(MenuGroup menuGroup) {
        if (!menuGroupRepository.existsById(menuGroup.getId())) {
            throw new MenuGroupException("존재하는 메뉴그룹 id가 없습니다", menuGroup.getId());
        }
    }
}
