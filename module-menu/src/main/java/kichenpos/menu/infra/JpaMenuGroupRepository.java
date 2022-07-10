package kichenpos.menu.infra;

import kichenpos.menu.domain.MenuGroup;
import kichenpos.menu.domain.MenuGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuGroupRepository extends JpaRepository<MenuGroup, Long>, MenuGroupRepository {
}
