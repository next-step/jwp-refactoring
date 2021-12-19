package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.application.fixture.MenuGroupFixture;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 그룹 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 메뉴그룹1;
    private MenuGroup 메뉴그룹2;

    @BeforeEach
    void setUp() {
        메뉴그룹1 = MenuGroupFixture.create(1L, "두마리메뉴");
        메뉴그룹2 = MenuGroupFixture.create(2L, "한마리메뉴");
    }

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        given(menuGroupDao.save(메뉴그룹1)).willReturn(메뉴그룹1);

        MenuGroup savedMenuGroup = menuGroupService.create(메뉴그룹1);

        assertThat(savedMenuGroup).isEqualTo(메뉴그룹1);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() {
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(메뉴그룹1, 메뉴그룹2));

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups).containsExactly(메뉴그룹1, 메뉴그룹2);
    }
}