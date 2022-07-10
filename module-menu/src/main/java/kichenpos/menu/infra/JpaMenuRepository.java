package kichenpos.menu.infra;

import kichenpos.menu.domain.Menu;
import kichenpos.menu.domain.MenuRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuRepository extends JpaRepository<Menu, Long>, MenuRepository {
}
