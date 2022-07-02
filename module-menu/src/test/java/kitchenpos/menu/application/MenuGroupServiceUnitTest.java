package kitchenpos.menu.application;

import static kitchenpos.menu.helper.MenuGroupFixtures.메뉴_그룹_만들기;
import static kitchenpos.menu.helper.MenuGroupFixtures.인기메뉴_그룹_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 그룹 Service 단위 테스트 - Stub")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceUnitTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupRepository);
    }

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        //given
        long generateMenuGroupId = 1;
        MenuGroupRequest request = 인기메뉴_그룹_요청;
        doAnswer(invocation -> 메뉴_그룹_만들기(generateMenuGroupId, request.getName()))
                .when(menuGroupRepository).save(any());

        //when
        MenuGroupResponse result = menuGroupService.create(request);

        //then
        assertThat(result.getId()).isEqualTo(generateMenuGroupId);
        assertThat(result.getName()).isEqualTo(request.getName());
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        //given
        MenuGroup menuGroup1 = 메뉴_그룹_만들기("인기 메뉴");
        MenuGroup menuGroup2 = 메뉴_그룹_만들기("단품 메뉴");
        MenuGroup menuGroup3 = 메뉴_그룹_만들기("세트 메뉴");
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(menuGroup1, menuGroup2, menuGroup3));

        //when
        List<MenuGroupResponse> results = menuGroupService.findAllMenuGroups();

        //then
        assertThat(results.stream().map(MenuGroupResponse::getName).collect(Collectors.toList()))
                .contains(menuGroup1.getName(), menuGroup2.getName(), menuGroup3.getName());

    }
}
