package kitchenpos.menugroup.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.generic.exception.MenuGroupNotFoundException;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroup create(final MenuGroupRequest menuGroup) {
        return menuGroupRepository.save(menuGroup.toEntity());
    }

    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }

    public MenuGroup findById(Long id) {
        return menuGroupRepository.findById(id)
            .orElseThrow(() -> new MenuGroupNotFoundException("해당 ID 의 메뉴그룹이 존재하지 않습니다."));
    }
}
