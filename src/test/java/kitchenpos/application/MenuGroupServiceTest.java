package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    private MenuGroup menuGroup1;
    private MenuGroup menuGroup2;

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroup1 = new MenuGroup();
        menuGroup1.setId(1L);
        menuGroup1.setName("두마리메뉴");

        menuGroup2 = new MenuGroup();
        menuGroup2.setId(2L);
        menuGroup2.setName("한마리메뉴");
    }

    @Test
    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    public void create() throws Exception {
        // given
        given(menuGroupDao.save(menuGroup1)).willReturn(menuGroup1);

        // when
        MenuGroup createdMenuGroup = menuGroupService.create(this.menuGroup1);

        // then
        assertThat(createdMenuGroup.getId()).isEqualTo(this.menuGroup1.getId());
        assertThat(createdMenuGroup.getName()).isEqualTo(this.menuGroup1.getName());
    }

    @Test
    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
    public void list() throws Exception {
        // given
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(menuGroup1, menuGroup2));

        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).containsExactly(menuGroup1, menuGroup2);
        assertThat(menuGroups.get(0).getId()).isEqualTo(menuGroup1.getId());
        assertThat(menuGroups.get(0).getName()).isEqualTo(menuGroup1.getName());
    }
}
