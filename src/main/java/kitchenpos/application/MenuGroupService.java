package kitchenpos.application;

import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupCreateRequest;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.global.exception.EntityNotFoundException;
import kitchenpos.mapper.MenuGroupMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroupResponse create(final MenuGroupCreateRequest request) {
        final MenuGroup savedMenuGroup = menuGroupRepository.save(MenuGroup.builder()
                .name(request.getName())
                .build());

        return MenuGroupMapper.toMenuGroupResponse(savedMenuGroup);
    }

    @Transactional(readOnly = true)
    public MenuGroup findMenuGroup(Long id) {
        return menuGroupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("menu not found. find menu id is %d", id)));
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        final List<MenuGroup> menuGroups = menuGroupRepository.findAll();

        return MenuGroupMapper.toMenuGroupResponses(menuGroups);
    }
}
