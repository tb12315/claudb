/*
 * Copyright (c) 2015-2017, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */

package com.github.tonivade.tinydb.command;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static javaslang.API.Case;
import static javaslang.API.Match;
import static javaslang.Predicates.instanceOf;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import com.github.tonivade.resp.protocol.RedisToken;
import com.github.tonivade.resp.protocol.SafeString;
import com.github.tonivade.tinydb.data.DatabaseValue;

// FIXME: refactor this ugly class
public class TinyDBResponse {

  public RedisToken addValue(DatabaseValue value) {
    if (value != null) {
      switch (value.getType()) {
      case STRING:
          SafeString string = value.getValue();
          return RedisToken.string(string);
      case HASH:
      case ZSET:
          Map<SafeString, SafeString> map = value.getValue();
          return RedisToken.array(keyValueList(map));
      case LIST:
      case SET:
          Collection<SafeString> list = value.getValue();
          return RedisToken.array(safeArrayList(list));
      default:
        break;
      }
    }
    return RedisToken.string(SafeString.EMPTY_STRING);
  }

  public RedisToken addArray(Collection<?> array) {
    if (array == null) {
      return RedisToken.array();
    }
    return RedisToken.array(array.stream().map(this::parseToken).collect(toList()));
  }

  private RedisToken parseToken(Object value) {
    return Match(value).of(
        Case(instanceOf(Integer.class), RedisToken::integer),
        Case(instanceOf(Boolean.class), RedisToken::integer),
        Case(instanceOf(String.class), RedisToken::string),
        Case(instanceOf(SafeString.class), RedisToken::string),
        Case(instanceOf(RedisToken.class), Function.identity()));
  }

  public RedisToken addArrayValue(Collection<DatabaseValue> array) {
    if (array != null) {
      return RedisToken.array(arrayValueList(array));
    }
    return RedisToken.array();
  }

  public RedisToken addSafeArray(Collection<SafeString> array) {
    if (array != null) {
      return RedisToken.array(safeArrayList(array));
    }
    return RedisToken.array();
  }

  public RedisToken addStringArray(Collection<String> array) {
    if (array != null) {
      return RedisToken.array(stringArrayList(array));
    }
    return RedisToken.array();
  }

  private List<RedisToken> arrayValueList(Collection<DatabaseValue> array) {
    return array.stream().map(Optional::ofNullable)
        .map(op -> op.map(identity()).orElse(null))
        .map(this::addValue)
        .collect(toList());
  }

  private List<RedisToken> keyValueList(Map<SafeString, SafeString> map) {
    return map.entrySet().stream()
        .flatMap(entry -> Stream.of(entry.getKey(), entry.getValue()))
        .map(RedisToken::string)
        .collect(toList());
  }

  private List<RedisToken> safeArrayList(Collection<SafeString> list) {
    return list.stream().map(RedisToken::string).collect(toList());
  }

  private List<RedisToken> stringArrayList(Collection<String> list) {
    return list.stream().map(RedisToken::string).collect(toList());
  }
}
