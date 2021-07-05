package kitchenpos.menu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public interface MenuProductDao extends JpaRepository<MenuProduct, Long> {
    @Query("select m from MenuGroup m where m.id in (:ids)")
    List<MenuProduct> findByProducts(@Param("ids")  List<Product> products);
}
