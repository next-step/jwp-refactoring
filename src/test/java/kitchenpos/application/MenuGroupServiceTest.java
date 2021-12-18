package kitchenpos.application;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

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
        given(menuGroupRepository.save(any())).willReturn(menuGroup);
        // when
        when(menuGroupRepository.save(any())).thenReturn(menuGroup);
        // then
        assertThat(menuGroupService.create(menuGroup)).isEqualTo(menuGroup);
        verify(menuGroupRepository, only()).save(any());
    }

    @Test
    @DisplayName("메뉴 그룹 리스트 조회")
    void listTest() {
        // given
        given(menuGroupRepository.findAll())
                .willReturn(Collections.singletonList(menuGroup));
        // when
        when(menuGroupService.list()).thenReturn(Collections.singletonList(menuGroup));
        List<MenuGroup> actual = menuGroupService.list();
        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual).containsExactly(menuGroup)
        );
    }
}

