package kitchenpos.menugroup.application;

import static kitchenpos.menugroup.domain.MenuGroupTestFixture.generateMenuGroup;
import static kitchenpos.menugroup.domain.MenuGroupTestFixture.generateMenuGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
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

    private MenuGroup 햄버거세트;
    private MenuGroup 햄버거단품;

    @BeforeEach
    void setUp() {
        햄버거세트 = generateMenuGroup(1L, "햄버거세트");
        햄버거단품 = generateMenuGroup(2L, "햄버거단품");
    }

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void createMenuGroup() {
        // given
        MenuGroupRequest menuGroupRequest = generateMenuGroupRequest(햄버거세트.getName());
        MenuGroup menuGroup = menuGroupRequest.toMenuGroup();
        given(menuGroupRepository.save(menuGroup)).willReturn(햄버거세트);

        // when
        MenuGroupResponse menuGroupResponse = menuGroupService.create(menuGroupRequest);

        // then
        assertAll(
                () -> assertThat(menuGroupResponse.getId()).isNotNull(),
                () -> assertThat(menuGroupResponse.getName()).isEqualTo(햄버거세트.getName().value())
        );
    }

    @DisplayName("메뉴 그룹명이 null이면 생성할 수 없다.")
    @Test
    void createMenuGroupThrowErrorWhenMenuGroupIsNull() {
        // given
        String name = null;
        MenuGroupRequest menuGroupRequest = generateMenuGroupRequest(name);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuGroupService.create(menuGroupRequest))
                .withMessage(ErrorCode.이름은_비어_있을_수_없음.getErrorMessage());
    }

    @DisplayName("메뉴 그룹명이 비어있이면 생성할 수 없다.")
    @Test
    void createMenuGroupThrowErrorWhenMenuGroupIsEmpty() {
        // given
        String name = "";
        MenuGroupRequest menuGroupRequest = generateMenuGroupRequest(name);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuGroupService.create(menuGroupRequest))
                .withMessage(ErrorCode.이름은_비어_있을_수_없음.getErrorMessage());
    }

    @DisplayName("메뉴 그룹 전체 목록을 조회한다.")
    @Test
    void findAllMenuGroups() {
        // given
        List<MenuGroup> menuGroups = Arrays.asList(햄버거세트, 햄버거단품);
        given(menuGroupRepository.findAll()).willReturn(menuGroups);

        // when
        List<MenuGroupResponse> findMenuGroups = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(findMenuGroups).hasSize(menuGroups.size()),
                () -> assertThat(findMenuGroups.stream()
                        .map(MenuGroupResponse::getName))
                        .containsExactly(햄버거세트.getName().value(),
                                햄버거단품.getName().value())
        );
    }
}
