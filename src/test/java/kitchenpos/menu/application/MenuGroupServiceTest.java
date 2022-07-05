package kitchenpos.menu.application;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroupDao;
import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void createMenuGroup() {
        // given
        MenuGroup 생성할_메뉴그룹 = new MenuGroup("커플코스A");
        given(menuGroupDao.save(생성할_메뉴그룹))
                .willReturn(new MenuGroup(1L, "커플코스A"));

        // when
        MenuGroup 생성된_메뉴그룹 = menuGroupService.create(생성할_메뉴그룹);

        // then
        메뉴그룹_생성_성공(생성된_메뉴그룹, 생성할_메뉴그룹);
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void listMenuGroup() {
        // given
        List<MenuGroup> 조회할_메뉴그룹_목록 = Arrays.asList(
                new MenuGroup(1L, "커플코스A"),
                new MenuGroup(2L, "커플코스B"),
                new MenuGroup(3L, "런치코스A")
        );
        given(menuGroupDao.findAll())
                .willReturn(조회할_메뉴그룹_목록);

        // when
        List<MenuGroup> 조회된_메뉴그룹_목록 = menuGroupService.list();

        // then
        메뉴그룹_목록_조회_성공(조회된_메뉴그룹_목록, 조회할_메뉴그룹_목록);
    }

    private void 메뉴그룹_생성_성공(MenuGroup actual, MenuGroup expected) {
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );
    }

    private void 메뉴그룹_목록_조회_성공(List<MenuGroup> actual, List<MenuGroup> expected) {
        assertThat(actual)
                .containsExactlyElementsOf(expected);
    }
}
