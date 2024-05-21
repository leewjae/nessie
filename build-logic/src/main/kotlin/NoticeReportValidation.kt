/*
 * Copyright (C) 2024 Dremio
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

import com.github.jk1.license.ProjectData
import com.github.jk1.license.filter.DependencyFilter
import gradle.kotlin.dsl.accessors._0f44f690aca8d7bb8fae2d09020d267b.licenseReport
import java.io.File
import org.gradle.api.GradleException

/**
 * Validates that all dependencies with MIT/BSD/Go/UPL/ISC licenses, which do not have an Apache
 * license, are mentioned in the `NOTICE` file.
 */
class NoticeReportValidation : DependencyFilter {
  fun needsNoNotice(license: String?): Boolean = license != null && (license.contains("Apache"))

  fun needsNotice(license: String?): Boolean =
    license != null &&
      (license.contains("MIT") ||
        license.contains("BSD") ||
        license.contains("Go") ||
        license.contains("ISC") ||
        license.contains("Universal Permissive"))

  override fun filter(data: ProjectData?): ProjectData {
    data!!

    val rootNoticeFile = data.project.rootProject.file("NOTICE").readText()

    val config = data.project.licenseReport

    val missing = mutableMapOf<String, String>()

    data.allDependencies.forEach { mod ->
      val licenses =
        (mod.manifests.map { it.license } +
            mod.licenseFiles.flatMap { it.fileDetails }.map { it.license } +
            mod.poms.flatMap { it.licenses }.map { it.name })
          .distinct()

      if (!licenses.any { needsNoNotice(it) } && licenses.any { needsNotice(it) }) {
        val groupModule = "${mod.group}:${mod.name}"
        if (!rootNoticeFile.contains(groupModule)) {
          missing.put(
            "${mod.group}:${mod.name}",
            """
            ---
            ${mod.group}:${mod.name}

            ${mod.licenseFiles.flatMap { it.fileDetails }.filter { it.file != null }.map { it.file }
              .map { File("${config.absoluteOutputDir}/$it").readText().trim() }
              .distinct()
              .map { "\n\n$it\n" }
              .joinToString("\n")
            }
            """
              .trimIndent()
          )
        }
      }
    }

    if (!missing.isEmpty()) {
      throw GradleException(
        "License information for the following artifacts is missing in the root NOTICE file: ${missing.map { it.value }.joinToString("\n")}"
      )
    }

    return data
  }
}