package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MenuGroupRepositoryTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("생성한 메뉴 그룹을 저장 한다")
    public void createMenuGroup() {
        //given
        String name = "피자";
        MenuGroup menuGroup = new MenuGroup(name);

        //when
        MenuGroup saveMenuGroup = menuGroupRepository.save(menuGroup);
        //then
        assertThat(saveMenuGroup).isEqualTo(menuGroup);
    }

    @Test
    @DisplayName("메뉴 그룹 리스트를 가져온다")
    public void selectMenuGroupList(){
        // when
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();

        // then
        assertThat(menuGroups).isNotEmpty();
        for (MenuGroup menuGroup : menuGroups) {
            assertThat(menuGroup.id()).isNotNull();
        }
    }
}
