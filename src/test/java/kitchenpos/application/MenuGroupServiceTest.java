package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 한마리메뉴;

    @BeforeEach
    void setUp() {
        한마리메뉴 = MenuGroupFixture.한마리메뉴;
    }

    @Test
    void 신규_메뉴_그룹을_등록하면_등록된_정보를_반환한다() {
        // given
        MenuGroup 한마리메뉴_등록_요청 = new MenuGroup("한마리메뉴");
        given(menuGroupDao.save(any())).willReturn(한마리메뉴);

        // when
        MenuGroup 한마리메뉴 = menuGroupService.create(한마리메뉴_등록_요청);

        // then
        메뉴_그룹_등록됨(한마리메뉴, 한마리메뉴_등록_요청.getName());
    }

    @Test
    void 메뉴_그룹_목록_조회시_등록된_메뉴_그룹_목록을_반환한다() {
        // given
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(한마리메뉴));

        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        메뉴_그룹_목록_조회됨(menuGroups, 한마리메뉴.getName());
    }

    private void 메뉴_그룹_등록됨(MenuGroup menuGroup, String name) {
        then(menuGroupDao).should(times(1)).save(any());
        assertThat(menuGroup.getName()).isEqualTo(name);
    }

    private void 메뉴_그룹_목록_조회됨(List<MenuGroup> menuGroups, String... expectedNames) {
        then(menuGroupDao).should(times(1)).findAll();
        List<String> groupNames = menuGroups.stream()
                .map(MenuGroup::getName)
                .collect(Collectors.toList());
        assertThat(groupNames).containsExactly(expectedNames);
    }
}
