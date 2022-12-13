package kitchenpos.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import kitchenpos.domain.MenuGroup;

@Repository
public class JdbcTemplateMenuGroupDao implements MenuGroupDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcTemplateMenuGroupDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public boolean existsById(final Long id) {
        final String sql = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM menu_group WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("id", id);
        return jdbcTemplate.queryForObject(sql, parameters, Boolean.class);
    }

    private MenuGroup toEntity(final ResultSet resultSet) throws SQLException {
        final MenuGroup entity = new MenuGroup();
        entity.setId(resultSet.getLong("id"));
        entity.setName(resultSet.getString("name"));
        return entity;
    }
}
