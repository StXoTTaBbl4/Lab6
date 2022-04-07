package Program.Common.CollectionInit;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Класс,используемый для корректной десериализации поля creationDate объектов класса Worker.
 */
public class ZonedDateTimeDeserializer implements JsonDeserializer<ZonedDateTime> {
    @Override
    public ZonedDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonArray TimeData = json.getAsJsonObject().getAsJsonArray("TimeData");

        String[] data = new String[8];
        int i=0;

        for(JsonElement s : TimeData){
            data[i]=s.getAsString();
            i++;
        }

        LocalDate localDate = LocalDate.of(Integer.parseInt(data[0]),
                                            Integer.parseInt(data[1]),
                                            Integer.parseInt(data[2]));

        LocalTime localTime = LocalTime.of(Integer.parseInt(data[3]),
                                            Integer.parseInt(data[4]),
                                            Integer.parseInt(data[5]),
                                            Integer.parseInt(data[6]));

        ZoneId zone = ZoneId.of(data[7]);

        return ZonedDateTime.of(localDate,localTime,zone);
    }
}
