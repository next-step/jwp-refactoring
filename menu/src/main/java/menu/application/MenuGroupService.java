package menu.application;

import menu.domain.MenuGroupRepository;
import menu.dto.MenuGroupRequest;
import menu.dto.MenuGroupResponse;
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
    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        return MenuGroupResponse.of(menuGroupRepository.save(menuGroupRequest.toEntity()));
    }

    public List<MenuGroupResponse> list() {
        return MenuGroupResponse.of(menuGroupRepository.findAll());
    }
}
