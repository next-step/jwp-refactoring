package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupDao;
import kitchenpos.menu.dto.MenuGroupCreateRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Test
    @DisplayName("메뉴 그룹을 등록한다.")
    void save() {
        // given
        String name = "두마리메뉴";
        MenuGroupCreateRequest request = new MenuGroupCreateRequest(name);
        MenuGroup menuGroup = MenuGroup.of(name);
        given(menuGroupDao.save(any())).willReturn(menuGroup);

        // when
        MenuGroupResponse result = menuGroupService.create(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(menuGroup.getName());
    }

    @Test
    @DisplayName("모든 메뉴 그룹을 조회한다.")
    void list() {
        // given
        List<MenuGroup> menuGroups = new ArrayList<>(Arrays.asList(MenuGroup.of("두마리메뉴"),
                MenuGroup.of("한마리메뉴"), MenuGroup.of("순살파닭두마리메뉴")));
        given(menuGroupDao.findAll()).willReturn(menuGroups);

        // when
        List<MenuGroupResponse> result = menuGroupService.list();

        // then
        assertThat(result.size()).isEqualTo(3);
    }
}
