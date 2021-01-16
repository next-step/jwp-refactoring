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

@DisplayName("메뉴 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    MenuGroupDao menuGroupDao;

    @InjectMocks
    MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void create() {
        //given
        given(menuGroupDao.save(any()))
                .willReturn(new MenuGroup(1L, "일반메뉴"));

        MenuGroup menuGroup = new MenuGroup(null, "일반메뉴");

        //when
        MenuGroup createMenuGroup = menuGroupService.create(menuGroup);

        //then
        assertThat(createMenuGroup.getId()).isNotNull();
        assertThat(createMenuGroup.getName()).isEqualTo("일반메뉴");
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void list() {
        //given
        given(menuGroupDao.findAll())
                .willReturn(
                        Arrays.asList(
                                new MenuGroup(1L, "일반메뉴"),
                                new MenuGroup(1L, "스페셜메뉴")
                        )
                );
        //when
        List<MenuGroup> menuGroups = menuGroupService.list();

        //then
        assertThat(menuGroups.size()).isEqualTo(2);
        assertThat(menuGroups.get(0).getName()).isEqualTo("일반메뉴");
        assertThat(menuGroups.get(1).getName()).isEqualTo("스페셜메뉴");
    }
}