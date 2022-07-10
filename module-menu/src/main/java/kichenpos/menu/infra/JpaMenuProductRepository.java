package kichenpos.menu.infra;

import kichenpos.menu.domain.MenuProduct;
import kichenpos.menu.domain.MenuProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuProductRepository extends JpaRepository<MenuProduct, Long>, MenuProductRepository {
}
