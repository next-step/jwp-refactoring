package kitchenpos.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.NoSuchElementException;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
    default MenuGroup getById(Long id) {
        return findById(id).orElseThrow(() -> new NoSuchElementException("메뉴 그룹을 찾을 수 없습니다. id: " + id));
    }
}
