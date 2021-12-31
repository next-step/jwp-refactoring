package kitchenpos.domain.menugroup.infra;

import kitchenpos.domain.menugroup.domain.MenuGroup;
import kitchenpos.domain.menugroup.domain.MenuGroupRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MenuGroupRepositoryImpl implements MenuGroupRepository {

    private final JpaMenuGroupRepository jpaMenuGroupRepository;

    public MenuGroupRepositoryImpl(JpaMenuGroupRepository jpaMenuGroupRepository) {
        this.jpaMenuGroupRepository = jpaMenuGroupRepository;
    }

    @Override
    public MenuGroup save(MenuGroup menuGroup) {
        return jpaMenuGroupRepository.save(menuGroup);
    }

    @Override
    public List<MenuGroup> findAll() {
        return jpaMenuGroupRepository.findAll();
    }

    @Override
    public boolean existsById(long id) {
        return jpaMenuGroupRepository.existsById(id);
    }
}
