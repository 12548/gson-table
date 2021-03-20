import cn.bugsnet.gsontable.GsonTable
import cn.bugsnet.gsontable.GsonTableAdapter
import cn.bugsnet.gsontable.NotGsonTableException
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

// Created by 12548 on 2021-03-20.

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GsonTableTest {

	private val gson: Gson = GsonBuilder().registerTypeAdapter(GsonTable::class.java, GsonTableAdapter()).create()

	@Test
	fun testAll() {
		val table = GsonTable(SampleClass::class.java, mutableListOf(
			SampleClass(1, "foo"),
			SampleClass(5, "bar"),
		))

		val json = gson.toJson(table)
		println(json)

		val resolvedTable = gson.fromJson<GsonTable<SampleClass>>(json, GsonTable::class.java)

		assertEquals(table, resolvedTable)

		assertThrows<NotGsonTableException> {
			gson.fromJson<GsonTable<SampleClass>>("{\"__classOfRow\":\"SampleClass\",\"cols\":[\"num\",\"str\"],\"rows\":[[1,\"foo\"],[5,\"bar\"]]}",
				GsonTable::class.java)
		}

		assertThrows<NotGsonTableException> {
			gson.toJson(GsonTable(Int::class.java, mutableListOf(1, 2, 3)))
		}
	}

}
