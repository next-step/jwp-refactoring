package kitchenpos.menu.application;

import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("메뉴그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupRepository menuGroupRepository;


    private MenuGroup 메뉴_그룹_기본;
    private MenuGroup 메뉴_그룹_요일;

    @BeforeEach
    void set_up() {
        메뉴_그룹_기본 = MenuGroupFixture.create("메뉴 그룹 기본");
        메뉴_그룹_요일 = MenuGroupFixture.create("메뉴 그룹 요일");
    }

    @Test
    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    void create_menu() {
        // given
        when(menuGroupRepository.save(any())).thenReturn(메뉴_그룹_기본);

        // when
        MenuGroupResponse 추천메뉴_등록 = menuGroupService.create(new MenuGroupRequest("메뉴그룹기본"));

        // then
        assertThat(추천메뉴_등록.getName()).isEqualTo(메뉴_그룹_기본.getName());
    }

    @Test
    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
    void find_menus() {
        // given
        when(menuGroupRepository.findAll()).thenReturn(Arrays.asList(메뉴_그룹_기본, 메뉴_그룹_요일));

        // when
        List<MenuGroupResponse> 메뉴그룹목록 = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(메뉴그룹목록).hasSize(2),
                () -> assertThat(메뉴그룹목록.get(0).getName()).isEqualTo(메뉴_그룹_기본.getName()),
                () -> assertThat(메뉴그룹목록.get(1).getName()).isEqualTo(메뉴_그룹_요일.getName())
        );
    }
}

