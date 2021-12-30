package kitchenpos.menu.group;

import kitchenpos.menu.group.domain.MenuGroup;
import kitchenpos.menu.group.domain.MenuGroupRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MenuGroupRepositoryTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("메뉴 그룹 생성")
    @Test
    void createMenuGroup() {

        //given
        final String menuGroupName = "중화요리";
        MenuGroup expectedMenuGroup = MenuGroup.create(menuGroupName);

        //when
        MenuGroup actualMenuGroup = menuGroupRepository.save(expectedMenuGroup);

        //then
        assertThat(actualMenuGroup).isNotNull();
        assertThat(actualMenuGroup.getId()).isGreaterThan(0L);
    }

    @DisplayName("메뉴 그룹 리스트 조회")
    @Test
    void getMenuGroups() {

        //given
        List<MenuGroup> menuGroups = Arrays.asList(MenuGroup.create("중화요리"), MenuGroup.create("일식"));
        menuGroupRepository.saveAll(menuGroups);

        //when
        List<MenuGroup> findMenuGroups = menuGroupRepository.findAll();

        //then
        Assertions.assertThat(findMenuGroups).isNotEmpty();
        Assertions.assertThat(findMenuGroups).extracting(MenuGroup::getName).contains("중화요리", "일식");
    }
}
