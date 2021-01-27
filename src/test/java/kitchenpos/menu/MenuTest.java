package kitchenpos.menu;

import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MenuTest {

    @Autowired
    private MenuDao menuDao;

    @Test
    @DisplayName("메뉴를 등록합니다.")
    void save() {
        // given
        String name = "맛있는치킨";
        Menu newMenu = MenuTestSupport.createMenu(name, 20000, 2L);

        // when
        Menu persistMenu = this.menuDao.save(newMenu);

        // then
        assertThat(persistMenu.getId()).isNotNull();
        assertThat(persistMenu.getName()).isEqualTo(name);
    }


    @Test
    @DisplayName("특정 메뉴를 조회합니다.")
    void findById() {
        // given
        String name = "맛있는치킨";
        Menu newMenu = MenuTestSupport.createMenu(name, 20000, 2L);
        Menu persistMenu = this.menuDao.save(newMenu);

        // when
        Menu foundMenu = this.menuDao.findById(persistMenu.getId()).get();

        // then
        assertThat(foundMenu.getId()).isEqualTo(persistMenu.getId());
        assertThat(foundMenu.getName()).isEqualTo(name);
    }


    @Test
    @DisplayName("전체 메뉴를 조회합니다.")
    void findAll() {
        // when
        List<Menu> foundMenuGroup = this.menuDao.findAll();

        // then
        assertThat(foundMenuGroup).hasSize(6);
    }


    @Test
    @DisplayName("주어진 메뉴ID로 메뉴가 몇개나 존재하는지 조회합니다.")
    void countByIdIn() {
        long countByIdIn = this.menuDao.countByIdIn(Arrays.asList(new Long[]{1L, 2L, 3L}));

        assertThat(countByIdIn).isEqualTo(3);
    }

}
