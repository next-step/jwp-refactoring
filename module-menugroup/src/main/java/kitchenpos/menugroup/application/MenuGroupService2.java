package kitchenpos.menugroup.application;

import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class MenuGroupService2 {

  private final MenuGroupRepository menuGroupRepository;

  public MenuGroupService2(MenuGroupRepository menuGroupRepository) {
    this.menuGroupRepository = menuGroupRepository;
  }

  public MenuGroupResponse create(final MenuGroupRequest request) {
    return MenuGroupResponse.from(menuGroupRepository.save(request.toEntity()));
  }

  @Transactional(readOnly = true)
  public List<MenuGroupResponse> findAllMenuGroups() {
    return MenuGroupResponse.ofList(menuGroupRepository.findAll());
  }
}
