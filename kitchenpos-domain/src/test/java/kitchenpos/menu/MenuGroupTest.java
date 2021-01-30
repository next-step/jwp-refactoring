package kitchenpos.menu;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class MenuGroupTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("메뉴 그룹을 등록합니다.")
    void save() {
        // given
        String name = "새로운메뉴";
        MenuGroup menuGroup = new MenuGroup(name);

        // when
        MenuGroup persistMenuGroup = this.menuGroupRepository.save(menuGroup);

        // then
        assertThat(persistMenuGroup.getId()).isNotNull();
        assertThat(persistMenuGroup.getName()).isEqualTo(name);
    }


    @Test
    @DisplayName("특정 메뉴 그룹을 조회합니다.")
    void findById() {
        // given
        String name = "새로운메뉴";
        MenuGroup menuGroup = new MenuGroup(name);
        MenuGroup persistMenuGroup = this.menuGroupRepository.save(menuGroup);

        // when
        MenuGroup foundMenuGroup = this.menuGroupRepository.findById(persistMenuGroup.getId()).get();

        // then
        assertThat(foundMenuGroup.getId()).isEqualTo(persistMenuGroup.getId());
        assertThat(foundMenuGroup.getName()).isEqualTo(name);
    }


    @Test
    @DisplayName("전체 메뉴 그룹을 조회합니다.")
    void findAll() {
        // when
        List<MenuGroup> foundMenuGroup = this.menuGroupRepository.findAll();

        // then
        assertThat(foundMenuGroup).hasSize(4);
    }

    @Test
    @DisplayName("ID로 해당 메뉴그룹의 존재 여부를 조회합니다.")
    void existsById() {
        // given
        String name = "새로운메뉴";
        MenuGroup menuGroup = new MenuGroup(name);
        MenuGroup persistMenuGroup = this.menuGroupRepository.save(menuGroup);

        // when
        boolean existsById = this.menuGroupRepository.existsById(persistMenuGroup.getId());

        // then
        assertTrue(existsById);
    }
}
