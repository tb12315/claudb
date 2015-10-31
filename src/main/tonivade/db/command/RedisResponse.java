/*
 * Copyright (c) 2015, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */

package tonivade.db.command;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import tonivade.db.data.DatabaseValue;
import tonivade.server.command.IResponse;
import tonivade.server.protocol.SafeString;

public class RedisResponse {

    private final IResponse response;

    public RedisResponse(IResponse response) {
        this.response = response;
    }

    public RedisResponse addValue(DatabaseValue value) {
        if (value != null) {
            switch (value.getType()) {
            case STRING:
                response.addBulkStr(value.getValue());
                break;
            case HASH:
                Map<SafeString, SafeString> map = value.getValue();
                response.addArray(keyValueList(map));
                break;
            case LIST:
            case SET:
            case ZSET:
                response.addArray(value.getValue());
                break;
            default:
                break;
            }
        } else {
            response.addBulkStr(null);
        }
        return this;
    }

    private List<SafeString> keyValueList(Map<SafeString, SafeString> map) {
        return map.entrySet().stream().flatMap(
                (entry) -> Stream.of(entry.getKey(), entry.getValue())).collect(toList());
    }

    public RedisResponse addArrayValue(Collection<DatabaseValue> array) {
        if (array != null) {
            response.addArray(array.stream().map(DatabaseValue::getValue).collect(toList()));
        } else {
            response.addArray(null);
        }
        return this;
    }

}