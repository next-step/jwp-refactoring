package kitchenpos.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import kitchenpos.tableGroup.domain.TableGroupRepository;

@Repository
public class JdbcTemplateTableGroupDao implements TableGroupRepository {
    private static final String TABLE_NAME = "table_group";
    private static final String KEY_COLUMN_NAME = "id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateTableGroupDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME)
        ;
    }

    @Override
    public kitchenpos.tableGroup.dto.TableGroupRequest save(final kitchenpos.tableGroup.dto.TableGroupRequest entity) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(entity);
        final Number key = jdbcInsert.executeAndReturnKey(parameters);
        return select(key.longValue());
    }

    @Override
    public Optional<kitchenpos.tableGroup.dto.TableGroupRequest> findById(final Long id) {
        try {
            return Optional.of(select(id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<kitchenpos.tableGroup.dto.TableGroupRequest> findAll() {
        final String sql = "SELECT id, created_date FROM table_group";
        return jdbcTemplate.query(sql, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private kitchenpos.tableGroup.dto.TableGroupRequest select(final Long id) {
        final String sql = "SELECT id, created_date FROM table_group WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.queryForObject(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private kitchenpos.tableGroup.dto.TableGroupRequest toEntity(final ResultSet resultSet) throws SQLException {
        final kitchenpos.tableGroup.dto.TableGroupRequest entity = new kitchenpos.tableGroup.dto.TableGroupRequest();
        entity.setId(resultSet.getLong(KEY_COLUMN_NAME));
        entity.setCreatedDate(resultSet.getObject("created_date", LocalDateTime.class));
        return entity;
    }
}
