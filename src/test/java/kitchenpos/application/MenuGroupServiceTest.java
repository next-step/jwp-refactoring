package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixtures.MenuGroupFixtures;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

/**
 * packageName : kitchenpos.application
 * fileName : MenuGroupServiceTest
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
@DisplayName("메뉴그룹 비즈니스 오브젝트 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    private MenuGroup menuGroup;

    @Mock
    MenuGroupDao menuGroupDao;

    @InjectMocks
    MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroup = MenuGroupFixtures.createMenuGroup(1L, "두마리메뉴");
    }

    @Test
    @DisplayName("메뉴 그룹을 조회할 수 있다.")
    public void list() {
        // given
        given(menuGroupDao.findAll()).willReturn(Lists.newArrayList(menuGroup, menuGroup));

        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).hasSize(2);
    }

    @Test
    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    public void create() {
        // given
        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(menuGroup);

        // when
        MenuGroup actual = menuGroupService.create(menuGroup);

        // then
        assertThat(actual).isEqualTo(menuGroup);
    }
}