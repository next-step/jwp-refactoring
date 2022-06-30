package kitchenpos.menugroup.appliaction;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.menugroup.fixture.MenuGroupFixture.강력_추천_메뉴;
import static kitchenpos.menugroup.fixture.MenuGroupFixture.추천_메뉴;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("메뉴 그룹 등록 테스트")
    void create() {
        MenuGroupRequest 요청_추천_메뉴 = MenuGroupRequest.of("추천 메뉴");

        when(menuGroupRepository.save(추천_메뉴)).thenReturn(추천_메뉴);

        MenuGroupResponse 추천_메뉴_등록_결과 = menuGroupService.create(요청_추천_메뉴);

        Assertions.assertThat(추천_메뉴_등록_결과).isEqualTo(MenuGroupResponse.of(추천_메뉴));
    }

    @Test
    @DisplayName("메뉴 그룹 조회 테스트")
    void findAll() {
        when(menuGroupRepository.findAll()).thenReturn(Arrays.asList(추천_메뉴, 강력_추천_메뉴));

        List<MenuGroupResponse> 추천_메뉴_조회_결과 = menuGroupService.list();

        assertAll(
                () -> Assertions.assertThat(추천_메뉴_조회_결과).hasSize(2),
                () -> Assertions.assertThat(추천_메뉴_조회_결과).containsExactly(
                        MenuGroupResponse.of(추천_메뉴),
                        MenuGroupResponse.of(강력_추천_메뉴)
                )
        );
    }
}
