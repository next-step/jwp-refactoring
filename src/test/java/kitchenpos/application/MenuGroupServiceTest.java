package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupCreateRequest;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.repository.MenuGroupRepository;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = MenuGroupFixture.한마리메뉴;
    }

    @Test
    @DisplayName("메뉴 그룹 등록시 등록에 성공하고 등록된 정보를 반환한다")
    void createMenuGroupThenReturnMenuGroupResponse() {
        // given
        MenuGroupCreateRequest request = new MenuGroupCreateRequest("한마리메뉴");
        given(menuGroupRepository.save(any())).willReturn(request.toMenuGroup());

        // when
        MenuGroupResponse menuGroupResponse = menuGroupService.createMenuGroup(request);

        // then
        then(menuGroupRepository).should(times(1)).save(any());
        assertThat(menuGroupResponse.compareRequest(request)).isTrue();
    }

    @Test
    @DisplayName("메뉴 그룹 목록 조회시 등록된 메뉴 그룹 목록을 반환한다")
    void findAllMenuGroupsThenMenuGroupResponses() {
        // given
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(menuGroup));

        // when
        List<MenuGroupResponse> menuGroups = menuGroupService.findAllMenuGroups();

        // then
        then(menuGroupRepository).should(times(1)).findAll();
        List<String> groupNames = menuGroups.stream()
                .map(MenuGroupResponse::getName)
                .collect(Collectors.toList());
        assertThat(groupNames).containsExactly(menuGroup.getName());
    }
}
