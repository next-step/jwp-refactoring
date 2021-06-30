package kitchenpos.dao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class JdbcTemplateMenuDao implements MenuDao {
    private static final String TABLE_NAME = "menu";
    private static final String KEY_COLUMN_NAME = "id";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcTemplateMenuDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public long countByIdIn(final List<Long> ids) {
        final String sql = "SELECT COUNT(*) FROM menu WHERE id IN (:ids)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("ids", ids);
        return jdbcTemplate.queryForObject(sql, parameters, Long.class);
    }
}
