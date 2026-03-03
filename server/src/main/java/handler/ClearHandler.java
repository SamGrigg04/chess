package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import service.ClearService;

import java.util.Map;

public class ClearHandler {
    private static final Gson serializer = new Gson();
    private final ClearService clearService;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    public void clear(Context ctx) {
        clearService.clear();
        ctx.status(200).result(serializer.toJson(Map.of()));
    }
}
