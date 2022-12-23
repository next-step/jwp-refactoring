package kitchenpos.application.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

@DisplayName("메뉴 그룹 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;;

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void createMenuGroup() {
        // given
        MenuGroup 커피류 = new MenuGroup("커피류");
        when(menuGroupRepository.save(any())).thenReturn(커피류);

        // when
        MenuGroupResponse result = menuGroupService.create(new MenuGroupRequest("커피류"));

        // then
        assertAll(
            () -> assertThat(result.getId()).isEqualTo(커피류.getId()),
            () -> assertThat(result.getName()).isEqualTo(커피류.getName())
        );
    }

    @DisplayName("메뉴 그룹을 조회한다.")
    @Test
    void findAllMenuGroup() {
        // given
        MenuGroup 커피류 = new MenuGroup("커피류");
        when(menuGroupRepository.findAll()).thenReturn(Arrays.asList(커피류));

        // when
        List<MenuGroupResponse> results = menuGroupService.list();

        // then
        assertAll(
            () -> assertThat(results).hasSize(1),
            () -> assertThat(results.get(0).getId()).isEqualTo(커피류.getId()),
            () -> assertThat(results.get(0).getName()).isEqualTo(커피류.getName())
        );
    }
}
