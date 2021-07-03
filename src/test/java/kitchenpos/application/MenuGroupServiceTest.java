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
        menuGroup1.setName("반반시리즈");

        menuGroup2 = new MenuGroup();
        menuGroup2.setId(2L);
        menuGroup2.setName("허니시리즈");
    }

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        given(menuGroupDao.save(menuGroup1)).willReturn(menuGroup1);

        MenuGroup createdMenuGroup = menuGroupService.create(this.menuGroup1);

        assertThat(createdMenuGroup.getId()).isEqualTo(this.menuGroup1.getId());
        assertThat(createdMenuGroup.getName()).isEqualTo(this.menuGroup1.getName());
    }

    @Test
    void list() {
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(menuGroup1, menuGroup2));

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups).containsExactly(menuGroup1, menuGroup2);
        assertThat(menuGroups.get(0).getName()).isEqualTo(menuGroup1.getName());
        assertThat(menuGroups.get(1).getName()).isEqualTo(menuGroup2.getName());
    }
}