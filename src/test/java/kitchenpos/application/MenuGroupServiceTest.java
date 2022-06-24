package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;
    @InjectMocks
    MenuGroupService menuGroupService;

    private MenuGroup 양식_메뉴;
    private MenuGroup 한식_메뉴;

    @BeforeEach
    void setUp() {
        양식_메뉴 = new MenuGroup( 1L, "양식_메뉴");
        한식_메뉴 = new MenuGroup(2L, "한식_메뉴");
    }

    @DisplayName("메뉴그룹을 등록할 수 있다")
    @Test
    void 메뉴그룹_등록(){
        //given
        MenuGroup menuGroup = new MenuGroup("양식_메뉴");
        when(menuGroupDao.save(menuGroup)).thenReturn(양식_메뉴);

        //when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        //then
        assertThat(savedMenuGroup.getId()).isEqualTo(양식_메뉴.getId());
    }

    @DisplayName("메뉴그룹의 목록을 조회할 수 있다")
    @Test
    void 메뉴그룹_목록_조회(){
        //given
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(양식_메뉴, 한식_메뉴));

        //when
        List<MenuGroup> list = menuGroupService.list();

        //then
        assertThat(list).containsExactly(양식_메뉴, 한식_메뉴);
    }
}