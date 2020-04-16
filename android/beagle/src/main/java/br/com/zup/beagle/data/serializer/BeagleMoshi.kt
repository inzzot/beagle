/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
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

package br.com.zup.beagle.data.serializer

import br.com.zup.beagle.data.serializer.adapter.ActionJsonAdapterFactory
import br.com.zup.beagle.data.serializer.adapter.AndroidFrameworkIgnoreAdapterFactory
import br.com.zup.beagle.data.serializer.adapter.ComponentJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

internal object BeagleMoshi {

    val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(AndroidFrameworkIgnoreAdapterFactory())
            .add(ComponentJsonAdapterFactory.make())
            .add(ActionJsonAdapterFactory.make())
            .add(KotlinJsonAdapterFactory())
            .build()
    }
}
