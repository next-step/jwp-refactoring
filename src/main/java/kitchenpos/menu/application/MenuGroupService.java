package kitchenpos.menu.application;

import kitchenpos.menu.dao.MenuGroupRepository;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroups;
import kitchenpos.menu.dto.MenuGroupCreateRequest;
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
    public MenuGroup create(final MenuGroupCreateRequest request) {
        return menuGroupRepository.save(request.of());
    }

    public MenuGroups list() {
        return new MenuGroups(menuGroupRepository.findAll());
    }

    public MenuGroup getMenuGroup(final Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id 는 null 이 아니여야 합니다.");
        }

        return menuGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 에 해당하는 메뉴 그룹을 찾을 수 없습니다."));
    }
}
