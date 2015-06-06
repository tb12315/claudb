/*
 * Copyright (c) 2015, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */

package tonivade.db.command.zset;

import static java.lang.String.valueOf;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static tonivade.db.data.DatabaseValue.score;
import static tonivade.db.data.DatabaseValue.zset;

import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import tonivade.db.command.ICommand;
import tonivade.db.command.IRequest;
import tonivade.db.command.IResponse;
import tonivade.db.command.annotation.Command;
import tonivade.db.command.annotation.ParamLength;
import tonivade.db.command.annotation.ParamType;
import tonivade.db.data.DataType;
import tonivade.db.data.DatabaseValue;
import tonivade.db.data.IDatabase;

@Command("zrangebyscore")
@ParamLength(3)
@ParamType(DataType.ZSET)
public class SortedSetRangeByScoreCommand implements ICommand {

    private static final String EMPTY_STRING = "";
    private static final String EXCLUSIVE = "(";
    private static final String MINUS_INFINITY = "-inf";
    private static final String INIFITY = "+inf";
    private static final String PARAM_WITHSCORES = "WITHSCORES";

    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        try {
            DatabaseValue value = db.getOrDefault(request.getParam(0), zset());
            NavigableSet<Entry<Float, String>> set = value.getValue();

            float from = parseRange(request.getParam(1));
            float to = parseRange(request.getParam(2));

            Set<Entry<Float, String>> range = set.subSet(
                    score(from, EMPTY_STRING), inclusive(request.getParam(1)),
                    score(to, EMPTY_STRING), inclusive(request.getParam(2)));

            List<String> result = emptyList();
            if (from <= to) {
                Optional<String> withScores = request.getOptionalParam(3);
                if (withScores.isPresent() && withScores.get().equals(PARAM_WITHSCORES)) {
                    result = range.stream().flatMap(
                            (o) -> Stream.of(valueOf(o.getKey()), o.getValue())).collect(toList());
                } else {
                    result = range.stream().map(
                            (o) -> o.getValue()).collect(toList());
                }
            }

            response.addArray(result);
        } catch (NumberFormatException e) {
            response.addError("ERR value is not an integer or out of range");
        }
    }

    private boolean inclusive(String param) {
        return !param.startsWith(EXCLUSIVE);
    }

    private float parseRange(String param) {
        switch (param) {
        case INIFITY:
            return Float.MAX_VALUE;
        case MINUS_INFINITY:
            return Float.MIN_VALUE;
        default:
            if (param.startsWith(EXCLUSIVE)) {
                return Float.parseFloat(param.substring(1));
            }
            return Float.parseFloat(param);
        }
    }

}