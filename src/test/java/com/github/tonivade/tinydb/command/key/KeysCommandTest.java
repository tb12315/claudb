/*
 * Copyright (c) 2015-2017, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */

package com.github.tonivade.tinydb.command.key;

import static com.github.tonivade.resp.protocol.SafeString.safeString;
import static com.github.tonivade.tinydb.data.DatabaseValue.string;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import com.github.tonivade.tinydb.command.CommandRule;
import com.github.tonivade.tinydb.command.CommandUnderTest;

@CommandUnderTest(KeysCommand.class)
public class KeysCommandTest {

    @Rule
    public final CommandRule rule = new CommandRule(this);

    @Captor
    private ArgumentCaptor<Collection<?>> captor;

    @Test
    public void testExecute() {
        rule.withData("abc", string("1"))
            .withData("acd", string("2"))
            .withData("c", string("3"))
            .withParams("*")
            .execute()
            .verify().addArray(captor.capture());

        Collection<?> value = captor.getValue();

        assertThat(value.size(), is(3));
        assertThat(value.contains(safeString("abc")), is(true));
        assertThat(value.contains(safeString("acd")), is(true));
        assertThat(value.contains(safeString("c")), is(true));
    }

    @Test
    public void testExecuteExclamation() {
        rule.withData("abc", string("1"))
            .withData("acd", string("2"))
            .withData("c", string("3"))
            .withParams("a??")
            .execute()
            .verify().addArray(captor.capture());

        Collection<?> value = captor.getValue();

        assertThat(value.size(), is(2));
        assertThat(value.contains(safeString("abc")), is(true));
        assertThat(value.contains(safeString("acd")), is(true));
        assertThat(value.contains(safeString("c")), is(false));
    }

    @Test
    public void testExecuteAsterisk() {
        rule.withData("abc", string("1"))
            .withData("acd", string("2"))
            .withData("c", string("3"))
            .withParams("a*")
            .execute()
            .verify().addArray(captor.capture());

        Collection<?> value = captor.getValue();

        assertThat(value.size(), is(2));
        assertThat(value.contains(safeString("abc")), is(true));
        assertThat(value.contains(safeString("acd")), is(true));
        assertThat(value.contains(safeString("c")), is(false));
    }

}
