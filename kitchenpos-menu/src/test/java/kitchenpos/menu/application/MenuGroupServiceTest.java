package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static kitchenpos.menu.domain.MenuGroupTestFixture.메뉴_그룹;
import static kitchenpos.menu.domain.MenuGroupTestFixture.메뉴_그룹_추천_메뉴;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 그룹 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        //given:
        final MenuGroup menuGroup = 메뉴_그룹_추천_메뉴();
        when(menuGroupRepository.save(menuGroup)).thenReturn(menuGroup);
        //when, then:
        assertThat(menuGroupService.create(menuGroup)).isEqualTo(메뉴_그룹_추천_메뉴());
    }

    @DisplayName("조회 성공")
    @Test
    void 조회_성공() {
        when(menuGroupRepository.findAll()).thenReturn(Collections.singletonList(메뉴_그룹_추천_메뉴()));
        assertThat(menuGroupService.list()).hasSize(1);
    }
}
