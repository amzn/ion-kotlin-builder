/*
 * Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amazon.ionkotlinbuilder

import org.junit.jupiter.api.Assertions.assertEquals
import com.amazon.ion.IonSymbol
import com.amazon.ion.IonValue
import com.amazon.ion.system.IonReaderBuilder
import com.amazon.ion.system.IonSystemBuilder
import com.amazon.ion.system.IonTextWriterBuilder
import java.io.ByteArrayOutputStream


internal val ion = IonSystemBuilder.standard().build()
internal val writerBuilder = IonTextWriterBuilder.standard()
internal val readerBuilder = IonReaderBuilder.standard()

internal val annotations = listOf("a", "b")
internal val domSymbol = ion.singleValue("symbol") as IonSymbol
internal val symbolToken = domSymbol.symbolValue()

internal fun reader(text: String) = readerBuilder.build(text)
internal fun readerValue(text: String) = reader(text).also { it.next() }


internal fun assertIon(
    expected: String,
    expectedIonJava: () -> IonValue? = { null },
    builder: IonWriterDsl.() -> Unit
) {

    val out = ByteArrayOutputStream()
    writerBuilder.build(out).use { writer ->
        writeIonWith(
            writer,
            builder
        )
    }

    val actualIon = ion.loader.load(out.toByteArray())
    val expectedIon = ion.loader.load(expected)

    assertEquals(expectedIon, actualIon)

    expectedIonJava()?.let { assertEquals(it, actualIon) }
}