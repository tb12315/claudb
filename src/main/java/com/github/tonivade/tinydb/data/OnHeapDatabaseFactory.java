/*
 * Copyright (c) 2017, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.tinydb.data;

public class OnHeapDatabaseFactory implements DatabaseFactory {

  @Override
  public Database create(String name) {
    return new OnHeapDatabase();
  }

  @Override
  public void clear() {
    // nothing to clear
  }
}