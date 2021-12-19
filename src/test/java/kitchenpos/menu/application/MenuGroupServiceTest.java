package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.fixture.MenuGroupFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@DisplayName("메뉴 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    MenuGroupRepository menuGroupRepository;

    @InjectMocks
    MenuGroupService menuGroupService;

    private MenuGroup 추천메뉴;
    private MenuGroup 사이드메뉴;

    @BeforeEach
    void setup() {
        추천메뉴 = MenuGroupFixture.create(1L, "추천메뉴");
        사이드메뉴 = MenuGroupFixture.create(1L, "사이드메뉴");
    }

    @DisplayName("메뉴 그룹 등록 확인")
    @Test
    void 메뉴_그룹_등록_확인() {
        // given
        MenuGroupRequest 메뉴_그룹_등록_요청_데이터 = MenuGroupRequest.of(추천메뉴.getName());

        doReturn(추천메뉴).when(menuGroupRepository)
                .save(any(MenuGroup.class));

        // when
        MenuGroupResponse 등록된_메뉴_그룹 = menuGroupService.create(메뉴_그룹_등록_요청_데이터);

        // then
        assertAll(
                () -> assertThat(등록된_메뉴_그룹.getId()).isNotNull(),
                () -> assertThat(등록된_메뉴_그룹.getName()).isEqualTo(메뉴_그룹_등록_요청_데이터.getName())
        );
    }

    @DisplayName("메뉴 그룹 목록 확인")
    @Test
    void 메뉴_그룹_목록_확인() {
        // given
        doReturn(Arrays.asList(추천메뉴, 사이드메뉴)).when(menuGroupRepository)
                .findAll();
        // when
        List<MenuGroupResponse> 메뉴_그룹_목록 = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(메뉴_그룹_목록).hasSize(2),
                () -> 상품_목록_조회_확인(메뉴_그룹_목록, Arrays.asList(추천메뉴, 사이드메뉴))
        );
    }

    private void 상품_목록_조회_확인(List<MenuGroupResponse> 조회된_메뉴_그룹_목록, List<MenuGroup> 요청된_메뉴_그룹_목록) {
        List<Long> 조회된_상품_ID_목록 = 조회된_메뉴_그룹_목록.stream()
                .map(MenuGroupResponse::getId)
                .collect(Collectors.toList());

        List<Long> 요청된_상품_ID_목록 = 요청된_메뉴_그룹_목록.stream()
                .map(MenuGroup::getId)
                .collect(Collectors.toList());

        assertThat(조회된_상품_ID_목록).containsExactlyElementsOf(요청된_상품_ID_목록);
    }

    @DisplayName("메뉴 목록 등록 실패 테스트")
    @Nested
    class CreateFailTest {
        @DisplayName("이름이 존재하지 않음")
        @Test
        void 이름이_존재하지_않음() {
            // given
            MenuGroupRequest 메뉴_그룹_등록_요청_데이터 = MenuGroupRequest.of(null);

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> menuGroupService.create(메뉴_그룹_등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("이름이 비어있음")
        @Test
        void 이름이_비어있음() {
            // given
            MenuGroupRequest 메뉴_그룹_등록_요청_데이터 = MenuGroupRequest.of("");

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> menuGroupService.create(메뉴_그룹_등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
