package menu.repository;

import org.springframework.data.jpa.repository.*;

import menu.domain.*;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
}
