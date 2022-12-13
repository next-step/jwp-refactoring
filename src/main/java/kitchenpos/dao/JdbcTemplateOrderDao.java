package kitchenpos.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateOrderDao implements OrderDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcTemplateOrderDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<String> orderStatuses) {
        final String sql = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END" +
            " FROM orders WHERE order_table_id = (:orderTableId) AND order_status IN (:orderStatuses)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("orderTableId", orderTableId)
            .addValue("orderStatuses", orderStatuses);
        return jdbcTemplate.queryForObject(sql, parameters, Boolean.class);
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,
        final List<String> orderStatuses) {
        final String sql = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END" +
            " FROM orders WHERE order_table_id IN (:orderTableIds) AND order_status IN (:orderStatuses)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("orderTableIds", orderTableIds)
            .addValue("orderStatuses", orderStatuses);
        return jdbcTemplate.queryForObject(sql, parameters, Boolean.class);
    }
}
