package kitchenpos.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MenuGroup {
    private Long id;
    private String name;

    protected MenuGroup() {}

    public MenuGroup(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroup from(final ResultSet resultSet) throws SQLException {
        return new MenuGroup(resultSet.getLong("id"), resultSet.getString("name"));
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
