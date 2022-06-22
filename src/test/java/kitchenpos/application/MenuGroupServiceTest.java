package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Mock
    private MenuGroupDao menuGroupDao;
    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    void create() {
        // given
        MenuGroup menuGroup = new MenuGroup.Builder().name("두마리통닭메뉴").build();

        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(new MenuGroup.Builder().id(1L).build());

        // when
        MenuGroup created = menuGroupService.create(menuGroup);

        // then
        assertThat(created.getId()).isNotNull();

        // verify
        then(menuGroupDao).should(times(1)).save(any(MenuGroup.class));
    }

    @Test
    void create_throwException_ifMissingName() {
        // given
        MenuGroup menuGroup = new MenuGroup.Builder().build();

        willThrow(new IllegalArgumentException()).given(menuGroupDao).save(menuGroup);

        // when
        // then
        assertThatThrownBy(() -> menuGroupService.create(menuGroup)).isInstanceOf(IllegalArgumentException.class);

        // verify
        then(jdbcTemplate).should(never()).queryForObject(anyString(), any(SqlParameterSource.class), any(RowMapper.class));
    }
}
