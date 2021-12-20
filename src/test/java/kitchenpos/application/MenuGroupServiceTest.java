package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.menu.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴그룹 기능 관리")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    private MenuGroupService menuGroupService;
    private MenuGroup 두마리치킨메뉴;


    @BeforeEach
    void setUp() {
        두마리치킨메뉴 = 메뉴그룹_생성("두마리치킨메뉴");
        menuGroupService = new MenuGroupService(menuGroupDao);
    }


    @Test
    @DisplayName("`메뉴그룹`을 등록할 수 있다.")
    void 메뉴그룹_등록() {
        // given
        when(menuGroupDao.save(any())).thenReturn(두마리치킨메뉴);

        // when
        MenuGroup 등록된_메뉴 = menuGroupService.create(두마리치킨메뉴);

        // then
        assertThat(등록된_메뉴).isNotNull();
    }

    @Test
    @DisplayName("`메뉴그룹`목록을 조회할 수 있다.")
    void 메뉴그룹_목록_조회() {
        // given
        when(menuGroupDao.findAll()).thenReturn(Collections.singletonList(두마리치킨메뉴));

        // when
        List<MenuGroup> 메뉴목록 = menuGroupService.list();

        // then
        assertThat(메뉴목록).contains(두마리치킨메뉴);
    }

    private MenuGroup 메뉴그룹_생성(String 메뉴그룹이름) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(메뉴그룹이름);
        return menuGroup;
    }
}
