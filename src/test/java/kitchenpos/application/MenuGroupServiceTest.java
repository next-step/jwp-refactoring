package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @Mock
    MenuGroupDao menuGroupDao;

    MenuGroup 한마리메뉴;
    MenuGroup 두마리메뉴;

    MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        한마리메뉴 = new MenuGroup(1L, "한마리메뉴");
        두마리메뉴 = new MenuGroup(2L, "두마리메뉴");

        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @DisplayName("메뉴 그룹을 등록")
    @Test
    void 메뉴그룹을_등록() {
        //given
        MenuGroup menuGroup = new MenuGroup("한마리메뉴");
        given(menuGroupDao.save(any())).willReturn(한마리메뉴);

        //when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        //then
        assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName());
        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @DisplayName("메뉴 그룹 목록을 불러옴")
    @Test
    void list() {
        //given
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(한마리메뉴, 두마리메뉴));

        //when
        List<MenuGroup> list = menuGroupService.list();

        //then
        assertThat(list).hasSize(2);
        assertThat(list).containsExactly(한마리메뉴, 두마리메뉴);
    }
}
