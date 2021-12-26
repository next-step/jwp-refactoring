package kitchenpos.menu;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.group.domain.MenuGroup;
import kitchenpos.menu.group.domain.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("메뉴 생성")
    @Test
    void createMenu() {

        //given
        Menu menu = Menu.prepared("후라이드세트", new BigDecimal("24000"));
        MenuGroup menuGroup = MenuGroup.create("치킨");
        MenuGroup actualMenuGroup = menuGroupRepository.save(menuGroup);
        menu.grouping(actualMenuGroup);

        //when
        Menu actualMenu = menuRepository.save(menu);

        //then
        assertThat(actualMenu.getId()).isGreaterThan(0L);
    }

    @DisplayName("메뉴 리스트 조회")
    @Test
    void getMenus() {

        //given
        MenuGroup menuGroup = MenuGroup.create("양식");
        MenuGroup actualMenuGroup = menuGroupRepository.save(menuGroup);

        Menu 후라이드세트 = Menu.prepared("후라이드세트", new BigDecimal("24000"));
        Menu 햄버거세트 = Menu.prepared("햄버거세트", new BigDecimal("10000"));
        후라이드세트.grouping(actualMenuGroup);
        햄버거세트.grouping(actualMenuGroup);

        List<Menu> saveMenus = Arrays.asList(후라이드세트, 햄버거세트);
        menuRepository.saveAll(saveMenus);

        //when
        List<Menu> findMenus = menuRepository.findAll();

        //then
        assertThat(findMenus).isNotEmpty();
        assertThat(findMenus.size()).isEqualTo(8);
    }
}
