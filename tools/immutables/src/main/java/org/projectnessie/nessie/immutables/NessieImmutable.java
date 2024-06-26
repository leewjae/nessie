/*
 * Copyright (C) 2023 Dremio
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.projectnessie.nessie.immutables;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.immutables.value.Value;

/**
 * A <a href="http://immutables.github.io/style.html#custom-immutable-annotation">Custom
 * {@code @Value.Immutable}</a> using {@code allParameters=true, lazyhash=true,
 * forceJacksonPropertyNames = false, clearBuilder = true}.
 */
@Documented
@Target(ElementType.TYPE)
@Value.Style(
    defaults = @Value.Immutable(lazyhash = true),
    allParameters = true,
    forceJacksonPropertyNames = false,
    clearBuilder = true,
    depluralize = true,
    get = {"get*", "is*"})
public @interface NessieImmutable {}
