package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
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

        given(menuGroupDao.save(any())).willReturn(menuGroup);

        //when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        //then
        assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("메뉴 그룹을 조회할 수 있다.")
    @Test
    void list() {
        // given
        List<MenuGroup> menuGroups = Arrays.asList(new MenuGroup(1L, "추천메뉴"),
            new MenuGroup(2L, "베스트메뉴"));

        given(menuGroupDao.findAll()).willReturn(menuGroups);

        //when
        List<MenuGroup> findMenuGroups = menuGroupService.list();

        //then
        assertThat(findMenuGroups.size()).isEqualTo(menuGroups.size());
        assertThat(findMenuGroups).containsAll(menuGroups);
    }
}
