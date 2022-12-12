package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
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

@DisplayName("메뉴 그룹 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void createMenuGroup() {
        // given
        MenuGroup menuGroup = new MenuGroup("피자");
        when(menuGroupRepository.save(menuGroup)).thenReturn(menuGroup);
        MenuGroupRequest menuGroupRequest = MenuGroupRequest.from(menuGroup.getName().value());

        // when
        MenuGroupResponse result = menuGroupService.create(menuGroupRequest);

        // then
        assertAll(
            () -> assertThat(result.getId()).isEqualTo(menuGroup.getId()),
            () -> assertThat(result.getName()).isEqualTo(menuGroup.getName().value())
        );
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void findAllMenuGroup() {
        // given
        MenuGroup menuGroup = new MenuGroup("피자");
        when(menuGroupRepository.findAll()).thenReturn(Arrays.asList(menuGroup));

        // when
        List<MenuGroupResponse> result = menuGroupService.list();

        // then
        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result.stream().map(MenuGroupResponse::getName))
                .containsExactly(menuGroup.getName().value())
        );
    }
}
