package kitchenpos.menu;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class MenuGroupTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Test
    @DisplayName("메뉴를 등록합니다.")
    void save() {
        // given
        String name = "새로운메뉴";
        MenuGroup menuGroup = MenuGroupTestSupport.createMenuGroup(name);

        // when
        MenuGroup persistMenuGroup = this.menuGroupDao.save(menuGroup);

        // then
        assertThat(persistMenuGroup.getId()).isNotNull();
        assertThat(persistMenuGroup.getName()).isEqualTo(name);
    }


    @Test
    @DisplayName("특정 메뉴를 조회합니다.")
    void findById() {
        // given
        String name = "새로운메뉴";
        MenuGroup menuGroup = MenuGroupTestSupport.createMenuGroup(name);
        MenuGroup persistMenuGroup = this.menuGroupDao.save(menuGroup);

        // when
        MenuGroup foundMenuGroup = this.menuGroupDao.findById(persistMenuGroup.getId()).get();

        // then
        assertThat(foundMenuGroup.getId()).isEqualTo(persistMenuGroup.getId());
        assertThat(foundMenuGroup.getName()).isEqualTo(name);
    }


    @Test
    @DisplayName("전체 메뉴를 조회합니다.")
    void findAll() {
        // when
        List<MenuGroup> foundMenuGroup = this.menuGroupDao.findAll();

        // then
        assertThat(foundMenuGroup).hasSize(4);
    }

    @Test
    @DisplayName("ID로 해당 메뉴그룹의 존재 여부를 조회합니다.")
    void existsById() {
        // given
        String name = "새로운메뉴";
        MenuGroup menuGroup = MenuGroupTestSupport.createMenuGroup(name);
        MenuGroup persistMenuGroup = this.menuGroupDao.save(menuGroup);

        // when
        boolean existsById = this.menuGroupDao.existsById(persistMenuGroup.getId());

        // then
        assertTrue(existsById);
    }
}
