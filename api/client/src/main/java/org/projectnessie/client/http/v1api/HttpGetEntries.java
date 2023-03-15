/*
 * Copyright (C) 2020 Dremio
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
package org.projectnessie.client.http.v1api;

import org.projectnessie.api.v1.params.EntriesParams;
import org.projectnessie.client.builder.BaseGetEntriesBuilder;
import org.projectnessie.client.http.NessieApiClient;
import org.projectnessie.error.NessieNotFoundException;
import org.projectnessie.model.EntriesResponse;

final class HttpGetEntries extends BaseGetEntriesBuilder<EntriesParams> {

  private final NessieApiClient client;

  HttpGetEntries(NessieApiClient client) {
    super(EntriesParams::forNextPage);
    this.client = client;
  }

  @Override
  protected EntriesParams params() {
    return EntriesParams.builder()
        .namespaceDepth(namespaceDepth)
        .filter(filter)
        .maxRecords(maxRecords)
        .hashOnRef(hashOnRef)
        .build();
  }

  @Override
  protected EntriesResponse get(EntriesParams p) throws NessieNotFoundException {
    if (withContent) {
      throw new IllegalArgumentException("'withContent' is not available with REST API v1");
    }
    return client.getTreeApi().getEntries(refName, p);
  }
}