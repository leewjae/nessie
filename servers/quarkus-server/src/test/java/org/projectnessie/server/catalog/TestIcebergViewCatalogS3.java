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
package org.projectnessie.server.catalog;

import static org.projectnessie.server.catalog.ObjectStorageMockTestResourceLifecycleManager.S3_WAREHOUSE_LOCATION;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import java.util.UUID;

@QuarkusTest
@TestProfile(S3UnitTestProfiles.S3UnitTestProfile.class)
public class TestIcebergViewCatalogS3 extends AbstractIcebergViewCatalogUnitTests {

  @Override
  protected String temporaryLocation() {
    return S3_WAREHOUSE_LOCATION + "/temp/" + UUID.randomUUID();
  }
}
