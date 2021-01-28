package kitchenpos.menu;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MenuTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("메뉴를 등록합니다.")
    void save() {
        // given
        String name = "맛있는치킨";
        Menu newMenu = MenuTestSupport.createMenu(name, 20000
                , this.menuGroupRepository.save(new MenuGroup("그룹1")));

        // when
        Menu persistMenu = this.menuRepository.save(newMenu);

        // then
        assertThat(persistMenu.getId()).isNotNull();
        assertThat(persistMenu.getName()).isEqualTo(name);
    }


    @Test
    @DisplayName("특정 메뉴를 조회합니다.")
    void findById() {
        // given
        String name = "맛있는치킨";
        Menu newMenu = MenuTestSupport.createMenu(name, 20000
                , this.menuGroupRepository.save(new MenuGroup("그룹1")));
        Menu persistMenu = this.menuRepository.save(newMenu);

        // when
        Menu foundMenu = this.menuRepository.findById(persistMenu.getId()).get();

        // then
        assertThat(foundMenu.getId()).isEqualTo(persistMenu.getId());
        assertThat(foundMenu.getName()).isEqualTo(name);
    }


    @Test
    @DisplayName("전체 메뉴를 조회합니다.")
    void findAll() {
        // when
        List<Menu> foundMenuGroup = this.menuRepository.findAll();

        // then
        assertThat(foundMenuGroup).hasSize(6);
    }


    @Test
    @DisplayName("주어진 메뉴ID로 메뉴가 몇개나 존재하는지 조회합니다.")
    void countByIdIn() {
        long countByIdIn = this.menuRepository.countByIdIn(Arrays.asList(new Long[]{1L, 2L, 3L}));

        assertThat(countByIdIn).isEqualTo(3);
    }

}
