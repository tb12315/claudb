package tonivade.db.command.hash;

import java.util.Map;

import tonivade.db.command.ICommand;
import tonivade.db.command.IRequest;
import tonivade.db.command.IResponse;
import tonivade.db.command.annotation.Command;
import tonivade.db.command.annotation.ParamLength;
import tonivade.db.command.annotation.ParamType;
import tonivade.db.data.DataType;
import tonivade.db.data.DatabaseValue;
import tonivade.db.data.IDatabase;

@Command("hget")
@ParamLength(2)
@ParamType(DataType.HASH)
public class HashGetCommand implements ICommand {

    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        DatabaseValue value = db.get(request.getParam(0));
        Map<String, String> map = value.getValue();
        response.addBulkStr(map.get(request.getParam(1)));
    }

}