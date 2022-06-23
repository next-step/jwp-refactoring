package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.application.fixture.MenuGroupFixtureFactory;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
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

    private MenuGroup 한식;
    private MenuGroup 중식;
    private MenuGroup 양식;

    @BeforeEach
    void before() {
        한식 = MenuGroupFixtureFactory.create(1L, "한식");
        중식 = MenuGroupFixtureFactory.create(2L, "중식");
        양식 = MenuGroupFixtureFactory.create(3L, "양식");
    }

    @DisplayName("메뉴 그룹을 생성 할 수 있다.")
    @Test
    void createTest() {

        //given
        MenuGroup 저장할_메뉴그룹 = new MenuGroup(1L, "메뉴그룹1");
        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(한식);

        //when
        MenuGroup menuGroup = menuGroupService.create(저장할_메뉴그룹);

        //then
        assertThat(menuGroup).isEqualTo(한식);
    }

    @DisplayName("메뉴 그릅의 목록을 조회 할 수 있다.")
    @Test
    void listTest() {

        //given
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(한식, 중식, 양식));

        //when
        List<MenuGroup> menuGroups = menuGroupService.list();

        //then
        assertThat(menuGroups).containsExactly(한식, 중식, 양식);
    }
}
