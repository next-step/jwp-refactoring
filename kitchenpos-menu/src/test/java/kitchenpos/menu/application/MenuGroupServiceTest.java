package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    private MenuGroup 분식;
    private MenuGroup 한식;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        분식 = new MenuGroup(1L,"분식");
        한식 = new MenuGroup(2L,"한식");
    }

    @Test
    void createMenuGroupTest() {
        //given
        when(menuGroupRepository.save(any(MenuGroup.class)))
                .thenReturn(분식);
        MenuGroupRequest request = new MenuGroupRequest(분식.getName());

        //when
        MenuGroupResponse menuGroup = menuGroupService.create(request);

        //then
        assertThat(menuGroup.getName())
                .isEqualTo(분식.getName());
    }

    @Test
    void retrieveMenuGroupsTest() {
        //given
        when(menuGroupRepository.findAll())
                .thenReturn(Arrays.asList(분식, 한식));

        //when
        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        //then
        assertThat(menuGroupResponsesToNames(menuGroups)).contains(
                분식.getName(), 한식.getName());
    }

    private List<String> menuGroupResponsesToNames(List<MenuGroupResponse> menuGroupResponses) {
        return menuGroupResponses.stream()
                .map(MenuGroupResponse::getName)
                .collect(Collectors.toList());
    }
}
