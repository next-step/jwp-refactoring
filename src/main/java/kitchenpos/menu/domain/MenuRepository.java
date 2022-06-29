package kitchenpos.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.NoSuchElementException;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    default Menu getById(Long id) {
        return findById(id).orElseThrow(() -> new NoSuchElementException("메뉴를 찾을 수 없습니다. id: " + id));
    }
}
