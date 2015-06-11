/*
 * Copyright (c) 2015, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */

package tonivade.db.command.list;

import static tonivade.db.data.DatabaseValue.list;

import java.util.ArrayList;
import java.util.List;

import tonivade.db.command.ICommand;
import tonivade.db.command.IRequest;
import tonivade.db.command.IResponse;
import tonivade.db.command.annotation.Command;
import tonivade.db.command.annotation.ParamLength;
import tonivade.db.command.annotation.ParamType;
import tonivade.db.data.DataType;
import tonivade.db.data.IDatabase;

@Command("lset")
@ParamLength(3)
@ParamType(DataType.LIST)
public class ListSetCommand implements ICommand {

    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        try {
            int index = Integer.parseInt(request.getParam(1));
            db.merge(request.getParam(0), list(),
                    (oldValue, newValue) -> {
                        List<String> merge = new ArrayList<>(oldValue.<List<String>>getValue());
                        merge.set(index > -1 ? index : merge.size() + index, request.getParam(2));
                        return list(merge);
                    });
            response.addSimpleStr("OK");
        } catch (NumberFormatException e) {
            response.addError("ERR value is not an integer or out of range");
        } catch (IndexOutOfBoundsException e) {
            response.addError("ERR index out of range");
        }
    }

}