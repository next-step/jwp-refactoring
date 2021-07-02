package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴그룹 서비스 관련 테스트")
public class MenuGroupServiceTest {

    private MenuGroupService menuGroupService;

    private Long menuGroupId1 = 1L;
    private Long menuGroupId2 = 1L;
    private String menuGroupName1 = "메뉴그룹1";
    private String menuGroupName2 = "메뉴그룹2";

    @Mock
    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @Test
    void create() {
        MenuGroup menuGroup1 = MenuGroup.of(menuGroupId1, menuGroupName1);

        when(menuGroupDao.save(any())).thenReturn(menuGroup1);
        MenuGroup menuGroupResponse = menuGroupService.create(menuGroup1);

        assertThat(menuGroupResponse.getId()).isEqualTo(menuGroupId1);
        assertThat(menuGroupResponse.getName()).isEqualTo(menuGroupName1);
    }

    @Test
    void findAll() {
        MenuGroup menuGroup1 = MenuGroup.of(menuGroupId1, menuGroupName1);
        MenuGroup menuGroup2 = MenuGroup.of(menuGroupId2, menuGroupName2);

        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(menuGroup1, menuGroup2));

        List<MenuGroup> menuGroupResponses = menuGroupService.list();

        assertThat(menuGroupResponses).contains(menuGroup1, menuGroup2);
    }

}
