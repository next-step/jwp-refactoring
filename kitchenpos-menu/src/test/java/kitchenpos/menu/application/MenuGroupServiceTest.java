package kitchenpos.menu.application;

import kitchenpos.menu.dto.MenuGroupCreateRequest;
import kitchenpos.menu.repository.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static kitchenpos.menu.domain.fixture.MenuGroupFixture.menuGroupA;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MenuGroupServiceTest")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("메뉴 그룹 생성")
    @Test
    void create() {

        BDDMockito.given(menuGroupRepository.save(ArgumentMatchers.any())).willReturn(menuGroupA());

        assertThat(menuGroupService.create(new MenuGroupCreateRequest(menuGroupA().getName())).getName())
                .isEqualTo(menuGroupA().getName());
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void name() {

        BDDMockito.given(menuGroupRepository.findAll()).willReturn(Collections.singletonList(menuGroupA()));

        assertThat(menuGroupService.list()).hasSize(1);
    }
}
