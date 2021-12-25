package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MenuGroupRepositoryTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("해당 id의 메뉴그룹의 존재 여부 조회")
    @Test
    void existById() {
        MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("추천메뉴"));
        boolean isExist = menuGroupRepository.existsById(savedMenuGroup.getId());
        assertThat(isExist).isTrue();
    }

}
