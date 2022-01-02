package kitchenpos.menu.service;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
    @InjectMocks
    MenuGroupService menuGroupService;

    @Mock
    MenuGroupRepository menuGroupRepository;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void 메뉴_그룹_생성() {
        // given
        MenuGroupRequest 튀김종류 = new MenuGroupRequest("튀김종류");
        MenuGroup expected = MenuGroup.of("튀김종류");
        given(menuGroupRepository.save(튀김종류.toMenuGroup())).willReturn(expected);

        // when
        MenuGroupResponse response = menuGroupService.create(튀김종류);

        // then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(expected.getId()),
                () -> assertThat(response.getName()).isEqualTo(튀김종류.getName())
        );
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void 메뉴_그룹_목록_조회() {
        // given
        MenuGroup 튀김종류_예상_결과 = MenuGroup.of("튀김종류");
        MenuGroup 중식종류_예상_결과 = MenuGroup.of("중식종류");
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(튀김종류_예상_결과, 중식종류_예상_결과));

        // when
        List<MenuGroupResponse> response = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(response.size()).isEqualTo(2),
                () -> assertThat(response).contains(MenuGroupResponse.of(튀김종류_예상_결과), MenuGroupResponse.of(중식종류_예상_결과))
        );
    }
}
