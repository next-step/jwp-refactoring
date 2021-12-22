package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupDao;
import kitchenpos.menu.testfixtures.MenuGroupTestFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void create() {
        // given
        String name = "추천메뉴";
        MenuGroup menuGroup = new MenuGroup(name);
        MenuGroupTestFixtures.메뉴그룹_생성_결과_모킹(menuGroupDao, menuGroup);

        //when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        //then
        assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("메뉴 그룹을 조회할 수 있다.")
    @Test
    void list() {
        // given
        List<MenuGroup> menuGroups = Arrays.asList(
            new MenuGroup(1L, "추천메뉴"),
            new MenuGroup(2L, "베스트메뉴"));
        MenuGroupTestFixtures.메뉴그룹_전체조회_모킹(menuGroupDao, menuGroups);

        //when
        List<MenuGroup> findMenuGroups = menuGroupService.list();

        //then
        assertThat(findMenuGroups.size()).isEqualTo(menuGroups.size());
        assertThat(findMenuGroups).containsAll(menuGroups);
    }
}
