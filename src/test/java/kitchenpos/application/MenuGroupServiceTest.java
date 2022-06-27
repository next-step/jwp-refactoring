package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    MenuGroupDao menuGroupDao;

    @InjectMocks
    MenuGroupService menuGroupService;

    MenuGroup 한마리메뉴;
    MenuGroup 두마리메뉴;

    @Test
    @DisplayName("메뉴그룹을 생성한다")
    void create() {
        //given
        한마리메뉴 = new MenuGroup(1L, "한마리메뉴");
        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(한마리메뉴);

        //when
        MenuGroup savedMenuGroup = menuGroupService.create(한마리메뉴);

        //then
        assertThat(savedMenuGroup).isNotNull()
                .satisfies(menuGroup -> {
                            menuGroup.getId().equals(1L);
                            menuGroup.getName().equals("한마리메뉴");
                        }
                );
    }

    @Test
    @DisplayName("메뉴그룹 전체 리스트를 조회한다")
    void list() {
        //given
        한마리메뉴 = new MenuGroup(1L, "한마리메뉴");
        두마리메뉴 = new MenuGroup(2L, "두마리메뉴");
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(한마리메뉴, 두마리메뉴));

        //when
        List<MenuGroup> menuGroups = menuGroupService.list();

        //then
        assertThat(menuGroups).containsExactlyInAnyOrderElementsOf(Arrays.asList(한마리메뉴, 두마리메뉴));
    }
}