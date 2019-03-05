/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ws2ten1.jackson.rawvalue;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test for {@link JacksonRawValueDeserializationModule}.
 */
public class JacksonRawValueDeserializationModuleTest {
	
	private ObjectMapper sut;
	
	
	@Before
	public void setUp() throws Exception {
		sut = new ObjectMapper();
		sut.registerModule(new JacksonRawValueDeserializationModule());
	}
	
	@Test
	public void testSerialize() throws Exception {
		// setup
		String json = "{\"str\":\"aa\",\"bool\":false,\"attributes\":{\"foo\":\"xx\",\"bar\":2}}";
		// exercise
		ExampleModel actual = sut.readValue(json, ExampleModel.class);
		// verify
		assertThat(actual)
			.returns("aa", ExampleModel::getStr)
			.returns(false, ExampleModel::isBool)
			.returns("{\"foo\":\"xx\",\"bar\":2}", ExampleModel::getAttributes);
	}
	
	@Test
	public void testDeserialize() throws Exception {
		// setup
		ExampleModel model = new ExampleModel("aa", false, "{\"foo\":\"xx\",\"bar\":2}");
		// exercise
		String actual = sut.writeValueAsString(model);
		// verify
		with(actual)
			.assertThat("$.str", is("aa"))
			.assertThat("$.bool", is(false))
			.assertThat("$.attributes.foo", is("xx"))
			.assertThat("$.attributes.bar", is(2));
	}
	
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class ExampleModel {
		
		private String str;
		
		private boolean bool;
		
		@JsonRawValue
		private String attributes;
	}
}
