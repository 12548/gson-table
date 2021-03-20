// Created by 12548 on 2021-03-20.


package cn.bugsnet.gsontable

import com.google.gson.*
import java.lang.reflect.Type


@Suppress("TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING")
class GsonTableAdapter : JsonSerializer<GsonTable<Any>>, JsonDeserializer<GsonTable<Any>> {

	companion object {
		const val KEY_GT = "__isTable"
		const val KEY_ELEMENT_CLASS = "__classOfRow"
		const val KEY_COLS = "cols"
		const val KEY_ROWS = "rows"
	}

	/**
	 * Gson invokes this call-back method during serialization when it encounters a field of the
	 * specified type.
	 *
	 *
	 * In the implementation of this call-back method, you should consider invoking
	 * [JsonSerializationContext.serialize] method to create JsonElements for any
	 * non-trivial field of the `src` object. However, you should never invoke it on the
	 * `src` object itself since that will cause an infinite loop (Gson will call your
	 * call-back method again).
	 *
	 * @param src the object that needs to be converted to Json.
	 * @param typeOfSrc the actual type (fully generalized version) of the source object.
	 * @return a JsonElement corresponding to the specified object.
	 */
	override fun serialize(src: GsonTable<Any>, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
		val res = JsonObject()
		res.add(KEY_GT, JsonPrimitive(true))
		res.add(KEY_ELEMENT_CLASS, JsonPrimitive(src.classOfElement.name))

		val cols = JsonArray()
		val rows = JsonArray()

		res.add(KEY_COLS, cols)
		res.add("rows", rows)

		src.map { context.serialize(it) }.forEach {
			if(!it.isJsonObject) throw NotGsonTableException()
			val obj = it.asJsonObject
			val row = JsonArray()

			obj.entrySet().forEach { (key, value) ->
				val wrappedKey = JsonPrimitive(key)
				if(!cols.contains(wrappedKey)) {
					cols.add(key)
				}

				val indexOfCol = cols.indexOf(wrappedKey)
				while (row.size() <= indexOfCol) row.add(JsonNull.INSTANCE)
				row[indexOfCol] = value
			}
			rows.add(row)
		}

		return res
	}

	/**
	 * Gson invokes this call-back method during deserialization when it encounters a field of the
	 * specified type.
	 *
	 * In the implementation of this call-back method, you should consider invoking
	 * [JsonDeserializationContext.deserialize] method to create objects
	 * for any non-trivial field of the returned object. However, you should never invoke it on the
	 * the same type passing `json` since that will cause an infinite loop (Gson will call your
	 * call-back method again).
	 *
	 * @param json The Json data being deserialized
	 * @param typeOfT The type of the Object to deserialize to
	 * @return a deserialized object of the specified type typeOfT which is a subclass of `T`
	 * @throws JsonParseException if json is not in the expected format of `typeofT`
	 */
	override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): GsonTable<Any> {
		val table = json.asJsonObject
		if(table.getAsJsonPrimitive(KEY_GT)?.asBoolean != true) throw NotGsonTableException()

		val elementClass = Class.forName(table.getAsJsonPrimitive(KEY_ELEMENT_CLASS).asString)
		val list = mutableListOf<Any>()

		val cols = table.getAsJsonArray(KEY_COLS)
		val rows = table.getAsJsonArray(KEY_ROWS)

		for (row in rows) {
			val obj = JsonObject()
			row.asJsonArray.forEachIndexed { index, jsonElement ->
				obj.add(cols[index].asString, jsonElement)
			}

			list.add(context.deserialize(obj, elementClass))
		}

		@Suppress("UNCHECKED_CAST")
		return GsonTable(elementClass as Class<Any>, list)
	}

}
