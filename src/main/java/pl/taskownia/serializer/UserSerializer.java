package pl.taskownia.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import pl.taskownia.model.User;

import java.io.IOException;

public class UserSerializer extends StdSerializer<User> {
    public UserSerializer(Class<User> t) {
        super(t);
    }

    public UserSerializer() {
        this(null);
    }

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", user.getId());
        jsonGenerator.writeStringField("username", user.getUsername());
        jsonGenerator.writeStringField("email", user.getEmail());
        jsonGenerator.writeObjectField("roles", user.getRoles());
        jsonGenerator.writeObjectField("status", user.getStatus());
        jsonGenerator.writeObjectField("created_at", user.getCreated_at());
        jsonGenerator.writeObjectField("updated_at", user.getUpdated_at()); //FIXME: nested personal data !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //FIXME: PERSONAL DATA + ADDRESS + IMAGE (image_path)
        jsonGenerator.writeEndObject();


    }
}
