package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.assertj.core.util.*;
import org.junit.jupiter.api.*;

import kitchenpos.dao.*;

@DisplayName("메뉴 그룹 관련 테스트")
class MenuGroupServiceTest {
    private MenuGroupDao menuGroupDao;
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroupDao = mock(MenuGroupDao.class);
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @DisplayName("메뉴그룹 생성하기")
    @Test
    void createTest() {
        when(menuGroupDao.save(menuGroup1)).thenReturn(menuGroup1);
        assertThat(menuGroupService.create(menuGroup1)).isEqualTo(menuGroup1);
    }

    @DisplayName("메뉴그룹 목록 반환하기")
    @Test
    void listTest() {
        when(menuGroupDao.findAll()).thenReturn(Lists.newArrayList(menuGroup1, menuGroup2, menuGroup3));
        assertThat(menuGroupService.list()).isEqualTo(Lists.newArrayList(menuGroup1, menuGroup2, menuGroup3));
    }

}
