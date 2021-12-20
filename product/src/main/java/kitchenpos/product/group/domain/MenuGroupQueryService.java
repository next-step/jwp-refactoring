package kitchenpos.product.group.domain;


import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class MenuGroupQueryService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupQueryService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public List<MenuGroup> findAll() {
        return menuGroupRepository.findAll();
    }
}
