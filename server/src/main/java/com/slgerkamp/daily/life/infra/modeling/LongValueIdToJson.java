package com.slgerkamp.daily.life.infra.modeling;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * <p>{@link LongValueId} JSON に 変換する。
 * <p> Jaskson を使ったJSON変換を行うための Serializer
 *
 */
public class LongValueIdToJson extends JsonSerializer<LongValueId> {

	@Override
	public void serialize(LongValueId value, JsonGenerator generator, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		generator.writeString(value.toString());
	}
}
