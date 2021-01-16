package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(idClass = Long.class, domainClass = Menu.class)
public interface MenuDao {
    Menu save(Menu entity);

    Optional<Menu> findById(Long id);

    List<Menu> findAll();

    long countByIdIn(List<Long> ids);
}
