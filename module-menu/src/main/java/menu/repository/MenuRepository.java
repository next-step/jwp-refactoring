package menu.repository;

import menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    long countByIdIn(List<Long> ids);
}
