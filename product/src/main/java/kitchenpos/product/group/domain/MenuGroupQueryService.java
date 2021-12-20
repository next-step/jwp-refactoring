package kitchenpos.product.group.domain;


import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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
