package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.fixture.MenuGroupFactory;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @InjectMocks
    private MenuGroupService menuGroupService;
    private MenuGroup 빅맥세트;
    private MenuGroup 상하이버거세트;

    @BeforeEach
    void setUp() {
        빅맥세트 = MenuGroupFactory.createMenuGroup(1L, "빅맥세트");
        상하이버거세트 = MenuGroupFactory.createMenuGroup(2L, "상하이버거세트");
    }

    @Test
    void 메뉴그룹_생성() {
        // given
        given(menuGroupRepository.save(any(MenuGroup.class))).willReturn(빅맥세트);

        // when
        MenuGroupResponse result = menuGroupService.create(
                MenuGroupFactory.createMenuGroupRequest(빅맥세트.getName().getValue()));

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(빅맥세트.getId()),
                () -> assertThat(result.getName()).isEqualTo(빅맥세트.getName().getValue())
        );
    }

    @Test
    void 메뉴그룹_목록_조회() {
        // given
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(빅맥세트, 상하이버거세트));

        // when
        List<MenuGroupResponse> result = menuGroupService.list();

        // then
        assertThat(toIdList(result)).containsExactlyElementsOf(
                Arrays.asList(빅맥세트.getId(), 상하이버거세트.getId()));
    }

    private List<Long> toIdList(List<MenuGroupResponse> menuGroupResponses) {
        return menuGroupResponses.stream()
                .map(MenuGroupResponse::getId)
                .collect(Collectors.toList());
    }
}
