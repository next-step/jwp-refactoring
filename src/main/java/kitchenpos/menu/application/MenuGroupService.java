package kitchenpos.menu.application;

import kitchenpos.menu.dao.MenuGroupRepository;
import kitchenpos.menu.domain.MenuGroup;
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
    public MenuGroup create(final MenuGroup menuGroup) {
        return menuGroupRepository.save(menuGroup);
    }

    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }

    public MenuGroup getMenuGroup(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id 는 null 이 아니여야 합니다.");
        }

        return menuGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 에 해당하는 메뉴 그룹을 찾을 수 없습니다."));
    }
}
