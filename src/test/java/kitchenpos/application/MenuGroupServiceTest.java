package kitchenpos.application;

import kitchenpos.common.domain.Name;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("MenuGroupService 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void createMenuGroup() {
        // given
        MenuGroup 한식 = new MenuGroup(new Name("한식"));
        when(menuGroupRepository.save(한식)).thenReturn(한식);

        // when
        MenuGroupResponse result = menuGroupService.create(MenuGroupRequest.of(한식.getName().value()));

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(한식.getId()),
                () -> assertThat(result.getName()).isEqualTo(한식.getName())
        );
    }

    @DisplayName("메뉴 그룹을 조회한다.")
    @Test
    void findAllMenuGroup() {
        // given
        MenuGroup 한식 = new MenuGroup(1L, new Name("한식"));
        when(menuGroupRepository.findAll()).thenReturn(Arrays.asList(한식));

        // when
        List<MenuGroupResponse> results = menuGroupService.findAll();

        // then
        assertAll(
                () -> assertThat(results).hasSize(1),
                () -> assertThat(results.get(0).getId()).isEqualTo(한식.getId()),
                () -> assertThat(results.get(0).getName()).isEqualTo(한식.getName())
        );
    }
}
