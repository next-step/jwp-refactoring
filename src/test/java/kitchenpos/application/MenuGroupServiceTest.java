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
        when(menuGroupDao.save(두마리메뉴_그룹)).thenReturn(두마리메뉴_그룹);
        assertThat(menuGroupService.create(두마리메뉴_그룹)).isEqualTo(두마리메뉴_그룹);
    }

    @DisplayName("메뉴그룹 목록 반환하기")
    @Test
    void listTest() {
        when(menuGroupDao.findAll()).thenReturn(Lists.newArrayList(두마리메뉴_그룹, 한마리메뉴_그룹, 순살파닭두마리메뉴_그룹));
        assertThat(menuGroupService.list()).isEqualTo(Lists.newArrayList(두마리메뉴_그룹, 한마리메뉴_그룹, 순살파닭두마리메뉴_그룹));
    }

}
