package kitchenpos.product.domain;

import kitchenpos.common.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

    default MenuGroup menuGroup(long id) {
        return findById(id).orElseThrow(
            () -> new NotFoundException(String.format("메뉴 그룹 id(%d)를 찾을 수 없습니다.", id)));
    }
}
