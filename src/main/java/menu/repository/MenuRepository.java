package menu.repository;

import org.springframework.data.jpa.repository.*;

import menu.domain.*;

public interface MenuRepository extends JpaRepository<Menu, Long> {

}
