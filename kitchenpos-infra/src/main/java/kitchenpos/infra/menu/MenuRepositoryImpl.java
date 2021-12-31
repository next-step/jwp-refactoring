package kitchenpos.infra.menu;

import kitchenpos.core.menu.domain.Menu;
import kitchenpos.core.menu.domain.MenuRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MenuRepositoryImpl implements MenuRepository {
    public MenuRepositoryImpl() {
    }

    private JpaMenuRepository jpaMenuRepository;

    public MenuRepositoryImpl(JpaMenuRepository jpaMenuRepository) {
        this.jpaMenuRepository = jpaMenuRepository;
    }

    @Override
    public Menu save(Menu menu) {
        return jpaMenuRepository.save(menu);
    }

    @Override
    public List<Menu> findAll() {
        return jpaMenuRepository.findAll();
    }

    @Override
    public int countByIdIn(List<Long> menuIds) {
        return jpaMenuRepository.countByIdIn(menuIds);
    }
}
