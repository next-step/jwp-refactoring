package kitchenpos.menu.application;


import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.persistence.MenuGroupRepository;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
    @InjectMocks
    private MenuGroupService menuGroupService;
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("메뉴그룹 추가할 경우 추가된 메뉴그룹정보를 반환")
    @Test
    public void returnMenuGroup() {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(Arbitraries.strings().ofMinLength(1).ofMaxLength(10).sample());
        doReturn(menuGroupRequest.toMenuGroup()).when(menuGroupRepository).save(any(MenuGroup.class));

        assertThat(menuGroupService.create(menuGroupRequest).getName()).isEqualTo(menuGroupRequest.getName());
    }

    @DisplayName("메뉴그룹목록을 조회할 경우 저장된 메뉴그룹목록반환")
    @Test
    public void returnMenuGroups() {

        List<MenuGroup> mockMenuGroups = Arrays.asList(
                MenuGroup.builder().id(1l).build(),
                MenuGroup.builder().id(2l).build(),
                MenuGroup.builder().id(3l).build());

        doReturn(mockMenuGroups).when(menuGroupRepository).findAll();

        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        List<Long> menuGroupIds = menuGroups.stream().map(MenuGroupResponse::getId).collect(Collectors.toList());
        assertAll(() -> assertThat(menuGroupIds).containsAnyOf(1l, 2l, 3l));
    }
}
