package kitchenpos.menu.application;

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
import java.util.Collections;
import java.util.List;

import static kitchenpos.menu.domain.MenuGroupTest.두마리메뉴;
import static kitchenpos.menu.domain.MenuGroupTest.한마리메뉴;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 그룹 관리 테스트")
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroup menuGroup;

    @Test
    @DisplayName("메뉴 그룹 생성")
    void createTest() {
        // given
        MenuGroupRequest 메뉴_그룹_요청 = new MenuGroupRequest("두마리메뉴");
        given(menuGroup.getName()).willReturn("두마리메뉴");
        given(menuGroupRepository.save(any())).willReturn(menuGroup);
        // when
        MenuGroupResponse menuGroupResponse = menuGroupService.create(메뉴_그룹_요청);
        // then
        assertThat(menuGroupResponse.getName()).isEqualTo(두마리메뉴.getName());
        verify(menuGroupRepository, only()).save(any());
    }

    @Test
    @DisplayName("메뉴 그룹 리스트 조회")
    void listTest() {
        // given
        MenuGroupResponse 두마리메뉴_응답 = MenuGroupResponse.of(두마리메뉴);
        MenuGroupResponse 한마리메뉴_응답 = MenuGroupResponse.of(한마리메뉴);
        given(menuGroupRepository.findAll())
                .willReturn(Arrays.asList(두마리메뉴, 한마리메뉴));
        // when
        List<MenuGroupResponse> actual = menuGroupService.list();
        // then
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual).containsExactly(두마리메뉴_응답, 한마리메뉴_응답)
        );
    }
}

