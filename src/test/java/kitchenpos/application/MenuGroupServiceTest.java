package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.assertj.core.util.*;
import org.junit.jupiter.api.*;

import kitchenpos.dao.*;
import kitchenpos.domain.*;

@DisplayName("메뉴 그룹 관련 테스트")
class MenuGroupServiceTest {
    private MenuGroupDao menuGroupDao;
    private MenuGroupService menuGroupService;
    private MenuGroup menuGroup1;
    private MenuGroup menuGroup2;
    private MenuGroup menuGroup3;

    @BeforeEach
    void setUp() {
        menuGroupDao = mock(MenuGroupDao.class);
        menuGroupService = new MenuGroupService(menuGroupDao);
        menuGroup1 = MenuGroup.of(1L, "두마리메뉴");
        menuGroup2 = MenuGroup.of(2L, "한마리메뉴");
        menuGroup3 = MenuGroup.of(3L, "순살파닭두마리메뉴");
    }

    @DisplayName("create메서드에 생성을 원하는 MenuGroup 객체를 인자로 하여 호출하면, 생성된 객체를 반환한다.")
    @Test
    void createTest() {
        when(menuGroupDao.save(menuGroup1)).thenReturn(menuGroup1);
        assertThat(menuGroupService.create(menuGroup1)).isEqualTo(menuGroup1);
    }

    @DisplayName("list메서드를 호출하면, 기 생성된 MenuGroup 목록을 반환한다.")
    @Test
    void listTest() {
        when(menuGroupDao.findAll()).thenReturn(Lists.newArrayList(menuGroup1, menuGroup2, menuGroup3));
        assertThat(menuGroupService.list()).isEqualTo(Lists.newArrayList(menuGroup1, menuGroup2, menuGroup3));
    }

}
