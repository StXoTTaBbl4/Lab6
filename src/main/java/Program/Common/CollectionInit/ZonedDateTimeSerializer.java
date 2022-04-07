package Program.Common.CollectionInit;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;

/**
 * Класс,используемый для корректной сериализации поля creationDate объектов класса Worker.
 */
public class ZonedDateTimeSerializer implements JsonSerializer<ZonedDateTime> {
    @Override
    public JsonElement serialize(ZonedDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        JsonArray array = new JsonArray();

        array.add(String.valueOf(src.getYear()));
        array.add(String.valueOf(src.getMonth().getValue()));
        array.add(String.valueOf(src.getDayOfMonth()));
        array.add(String.valueOf(src.getHour()));
        array.add(String.valueOf(src.getMinute()));
        array.add(String.valueOf(src.getSecond()));
        array.add(String.valueOf(src.getNano()));
        array.add(String.valueOf(src.getZone()));

        jsonObject.add("TimeData", array);

        return jsonObject;
    }
}
