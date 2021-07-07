package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 그룹 관리")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @DisplayName("메뉴 그룹을 추가한다.")
    @Test
    void create() {
        //given
        MenuGroup menuGroup = MenuGroup.of(1L, "후라이드반 양념반");

        //and
        given(menuGroupDao.save(any())).willReturn(menuGroup);

        //when
        MenuGroup actual = menuGroupService.create(menuGroup);

        //then
        assertThat(actual.getId()).isEqualTo(menuGroup.getId());
        assertThat(actual.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("메뉴 그룹의 이름을 지정해야한다.")
    @Test
    void createMenuGroupExceptionIfNameIsNull() {
        //TODO: 추가 기능 개발
    }

    @DisplayName("메뉴 그룹을 모두 조회한다.")
    @Test
    void list() {
        //given
        MenuGroup 착한세트 = MenuGroup.of(1L, "착한세트");
        MenuGroup 더착한세트 = MenuGroup.of(2L, "더착한세트");
        List<MenuGroup> menuGroups = Lists.list(착한세트, 더착한세트);

        //and
        given(menuGroupDao.findAll()).willReturn(menuGroups);

        //when
        List<MenuGroup> actual = menuGroupService.list();

        //then
        assertThat(actual.size()).isEqualTo(2);
        for (int i = 0; i < 2; i++) {
            assertThat(actual.get(i).getId()).isEqualTo(menuGroups.get(i).getId());
            assertThat(actual.get(i).getName()).isEqualTo(menuGroups.get(i).getName());
        }
    }
}