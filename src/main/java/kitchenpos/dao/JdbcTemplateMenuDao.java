package kitchenpos.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateMenuDao implements MenuDao {
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
