package kitchenpos.table.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class TableStatusConverter implements AttributeConverter<TableStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TableStatus tableStatus) {
        return tableStatus.statusCode;
    }

    @Override
    public TableStatus convertToEntityAttribute(Integer dbData) {
        return Stream.of(TableStatus.values())
                .filter(tableStatus -> tableStatus.statusCode == dbData)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("일치하는 테이블 상태가 없습니다."));
    }
}
