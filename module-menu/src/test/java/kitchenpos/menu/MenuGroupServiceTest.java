package kitchenpos.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.dao.MenuGroupRepository;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @InjectMocks
    MenuGroupService menuGroupService;

    @Mock
    MenuGroupRepository menuGroupRepository;

    MenuGroup 한마리메뉴 = new MenuGroup("한마리메뉴");

    @Test
    @DisplayName("메뉴 그룹을 저장한다")
    void create() {
        // given
        given(menuGroupRepository.save(any())).willReturn(한마리메뉴);

        // when
        MenuGroupResponse actual = menuGroupService.create(new MenuGroupRequest("한마리메뉴"));

        // then
        assertThat(actual.getName()).isEqualTo("한마리메뉴");
    }

    @Test
    @DisplayName("메뉴 그룹 리스트를 조회한다")
    void list() {
        // given
        given(menuGroupRepository.findAll()).willReturn(Collections.singletonList(한마리메뉴));

        // when
        List<MenuGroupResponse> actual = menuGroupService.list();

        // then
        assertThat(actual).hasSize(1);
    }
}
