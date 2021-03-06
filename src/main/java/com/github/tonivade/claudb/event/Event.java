/*
 * Copyright (c) 2015-2018, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.claudb.event;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import com.github.tonivade.purefun.typeclasses.Equal;
import com.github.tonivade.resp.protocol.SafeString;

public abstract class Event {
  private SafeString command;
  private SafeString key;
  private int schema;

  public Event(SafeString command, SafeString key, int schema) {
    this.command = requireNonNull(command);
    this.key = requireNonNull(key);
    this.schema = schema;
  }

  public SafeString getCommand() {
    return command;
  }

  public SafeString getKey() {
    return key;
  }

  public int getSchema() {
    return schema;
  }

  public abstract String getChannel();
  public abstract SafeString getValue();

  @Override
  public boolean equals(Object obj) {
    return Equal.of(this)
         .append((o1, o2) -> Objects.equals(o1.command, o2.command))
         .append((o1, o2) -> Objects.equals(o1.key, o2.key))
         .append((o1, o2) -> Objects.equals(o1.schema, o2.schema))
         .applyTo(obj);
  }

  @Override
  public int hashCode() {
    return Objects.hash(command, key, schema);
  }

  public static KeyEvent keyEvent(SafeString command, SafeString key, int schema) {
    return new KeyEvent(command, key, schema);
  }

  public static KeySpace commandEvent(SafeString command, SafeString key, int schema) {
    return new KeySpace(command, key, schema);
  }
}
