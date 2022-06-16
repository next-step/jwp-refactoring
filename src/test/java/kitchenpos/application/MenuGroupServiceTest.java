package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.application.fixture.MenuGroupFixtureFactory;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 분식_메뉴그룹;
    private MenuGroup 초밥_메뉴그룹;

    @BeforeEach
    void setUp() {
        분식_메뉴그룹 = MenuGroupFixtureFactory.create(1L, "분식 메뉴그룹");
        초밥_메뉴그룹 = MenuGroupFixtureFactory.create(2L, "초밥 메뉴그룹");
    }

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void create01() {
        // given
        MenuGroup menuGroup = MenuGroup.from("초밥_메뉴그룹");
        given(menuGroupDao.save(menuGroup)).willReturn(초밥_메뉴그룹);

        // when
        MenuGroup actualMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(actualMenuGroup).isEqualTo(초밥_메뉴그룹);
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void find01() {
        // given
        given(menuGroupDao.findAll()).willReturn(Lists.newArrayList(분식_메뉴그룹, 초밥_메뉴그룹));

        // when
        List<MenuGroup> actualMenuGroups = menuGroupService.list();

        // then
        assertThat(actualMenuGroups).containsExactly(분식_메뉴그룹, 초밥_메뉴그룹);
    }
}