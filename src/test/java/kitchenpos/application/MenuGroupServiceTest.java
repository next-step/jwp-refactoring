package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 그룹 관련 기능 테스트")
@ExtendWith(MockitoExtension.class)
public
class MenuGroupServiceTest {
    private MenuGroup menuGroup1;
    private MenuGroup menuGroup2;

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroup1 = 메뉴_그룹_생성(1L, "반반시리즈");
        menuGroup2 = 메뉴_그룹_생성(2L, "허니시리즈");
    }

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        given(menuGroupDao.save(menuGroup1)).willReturn(menuGroup1);

        MenuGroup createdMenuGroup = 메뉴_그룹_생성_요청(this.menuGroup1);

        메뉴_그룹_생성됨(createdMenuGroup, this.menuGroup1);
    }

    @Test
    void list() {
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(menuGroup1, menuGroup2));

        List<MenuGroup> createdMenuGroups = 메뉴_그룹_리스트_요청();

        메뉴_그룹_리스트_요청됨(createdMenuGroups, Arrays.asList(menuGroup1, menuGroup2));
    }

    public static MenuGroup 메뉴_그룹_생성(Long id, String name) {
        return new MenuGroup(id, name);
    }

    private void 메뉴_그룹_생성됨(MenuGroup createdMenuGroup, MenuGroup menuGroup1) {
        assertThat(createdMenuGroup.getId()).isEqualTo(menuGroup1.getId());
        assertThat(createdMenuGroup.getName()).isEqualTo(menuGroup1.getName());
    }

    private MenuGroup 메뉴_그룹_생성_요청(MenuGroup menuGroup) {
        return menuGroupService.create(menuGroup);
    }

    private List<MenuGroup> 메뉴_그룹_리스트_요청() {
        return menuGroupService.list();
    }

    private void 메뉴_그룹_리스트_요청됨(List<MenuGroup> createdMenuGroups, List<MenuGroup> menuGroups) {
        assertThat(createdMenuGroups).containsExactly(menuGroups.get(0), menuGroups.get(1));
        assertThat(createdMenuGroups.get(0).getName()).isEqualTo(menuGroups.get(0).getName());
        assertThat(createdMenuGroups.get(1).getName()).isEqualTo(menuGroups.get(1).getName());
    }
}