package kitchenpos.application.command;

import kitchenpos.application.query.MenuGroupQueryService;
import kitchenpos.domain.Name;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupCreate;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.dto.response.MenuGroupViewResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    private MenuGroupService menuGroupService;
    private MenuGroupQueryService menuGroupQueryService;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupRepository);
        menuGroupQueryService = new MenuGroupQueryService(menuGroupRepository);
    }

    @Test
    @DisplayName("create - 정상적인 메뉴 그룹 저장")
    void 정상적인_메뉴_그룹_저장() {
        // given
        MenuGroupCreate menuGroupCreate = new MenuGroupCreate("Hello");

        // when
        when(menuGroupRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        menuGroupService.create(menuGroupCreate);

        // then

        verify(menuGroupRepository, VerificationModeFactory.times(1))
                .save(any());
    }

    @Test
    @DisplayName("list - 정상적인 메뉴 그룹 조회")
    void 정상적인_메뉴_그룹_조회() {
        // given
        List<MenuGroup> menuGroups = Arrays.asList(
                new MenuGroup(1L, new Name("A")),
                new MenuGroup(2L, new Name("B")),
                new MenuGroup(3L, new Name("C"))
        );

        // when
        when(menuGroupRepository.findAll())
                .thenReturn(menuGroups);

        List<MenuGroupViewResponse> list = menuGroupQueryService.list();

        // then
        List<MenuGroupViewResponse> responses = menuGroups.stream()
                .map(MenuGroupViewResponse::of)
                .collect(Collectors.toList());

        assertThat(list)
                .containsExactlyElementsOf(responses);

        verify(menuGroupRepository, VerificationModeFactory.times(1))
                .findAll();
    }
}