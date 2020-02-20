package br.com.zup.beagle.data.serializer.adapter

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type

internal class AndroidFrameworkIgnoreAdapterFactory : JsonAdapter.Factory {

    override fun create(
        type: Type,
        annotations: MutableSet<out Annotation>,
        moshi: Moshi
    ): JsonAdapter<*>? {
        return if (isTypeFromAndroid(type)) {
            AndroidFrameworkIgnoreAdapter(moshi.adapter(type, annotations))
        } else {
            null
        }
    }

    private fun isTypeFromAndroid(type: Type): Boolean {
        listOf(View::class.java, Context::class.java, Fragment::class.java).forEach {
            val superclass = Types.getRawType(type).superclass as? Class<Any>

            if (checkIfSuperClassIsTypeOf(superclass, it)) {
                return true
            }
        }

        return false
    }

    private fun checkIfSuperClassIsTypeOf(superclass: Class<in Any>?, clazz: Class<out Any>): Boolean {
        return when (superclass) {
            null -> false
            clazz -> true
            else -> checkIfSuperClassIsTypeOf(superclass.superclass, clazz)
        }
    }
}

internal class AndroidFrameworkIgnoreAdapter(
    private val delegate: JsonAdapter<Any>
) : JsonAdapter<Any>() {

    override fun fromJson(reader: JsonReader): Any? {
        reader.skipValue()
        return null
    }

    override fun toJson(writer: JsonWriter, value: Any?) {
        delegate.toJson(writer, null)
    }
}