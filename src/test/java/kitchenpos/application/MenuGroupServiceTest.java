package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void createMenuGroup() {
        // given
        MenuGroup menuGroup = 메뉴_그룹_생성("두마리메뉴");
        given(menuGroupDao.save(any()))
            .willReturn(menuGroup);

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertEquals(menuGroup, savedMenuGroup);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void getMenuGroups() {
        // given
        List<MenuGroup> menuGroups = Arrays.asList(
            메뉴_그룹_생성("두마리메뉴"),
            메뉴_그룹_생성("한마리메뉴"));
        given(menuGroupDao.findAll())
            .willReturn(menuGroups);

        // when
        List<MenuGroup> findMenuGroups = menuGroupService.list();

        // then
        assertThat(findMenuGroups)
            .containsExactlyElementsOf(menuGroups);
    }

    static MenuGroup 메뉴_그룹_생성(String name) {
        return new MenuGroup(name);
    }

    static MenuGroup 메뉴_그룹_생성(Long id, String name) {
        return new MenuGroup(id, name);
    }
}
