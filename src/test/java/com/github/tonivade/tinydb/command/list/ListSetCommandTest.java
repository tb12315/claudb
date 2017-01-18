/*
 * Copyright (c) 2015-2017, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */

package com.github.tonivade.tinydb.command.list;

import static com.github.tonivade.tinydb.DatabaseValueMatchers.isList;
import static com.github.tonivade.tinydb.DatabaseValueMatchers.list;
import static org.mockito.Matchers.startsWith;

import org.junit.Rule;
import org.junit.Test;

import com.github.tonivade.tinydb.command.CommandRule;
import com.github.tonivade.tinydb.command.CommandUnderTest;
import com.github.tonivade.tinydb.command.list.ListSetCommand;

@CommandUnderTest(ListSetCommand.class)
public class ListSetCommandTest {

    @Rule
    public final CommandRule rule = new CommandRule(this);

    @Test
    public void testExecute() throws Exception {
        rule.withData("key", list("a", "b", "c"))
            .withParams("key", "0", "A")
            .execute()
            .assertValue("key", isList("A", "b", "c"))
            .verify().addSimpleStr("OK");

        rule.withData("key", list("a", "b", "c"))
            .withParams("key", "-1", "C")
            .execute()
            .assertValue("key", isList("a", "b", "C"))
            .verify().addSimpleStr("OK");

        rule.withData("key", list("a", "b", "c"))
            .withParams("key", "z", "C")
            .execute()
            .assertValue("key", isList("a", "b", "c"))
            .verify().addError(startsWith("ERR"));

        rule.withData("key", list("a", "b", "c"))
            .withParams("key", "99", "C")
            .execute()
            .assertValue("key", isList("a", "b", "c"))
            .verify().addError(startsWith("ERR"));
    }

}
