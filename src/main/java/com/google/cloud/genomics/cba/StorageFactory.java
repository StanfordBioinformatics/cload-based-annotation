package com.google.cloud.genomics.cba;

/*
 * Copyright (C) 2016-2018 Stanford University.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collection;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;


/**
 * This class manages the details of creating a Storage service, including auth.
 */

public class StorageFactory {
  private static Storage instance = null;

  public static synchronized Storage getService() throws IOException, GeneralSecurityException {
    if (instance == null) {
      instance = buildService();
    }
    return instance;
  }

  private static Storage buildService() throws IOException, GeneralSecurityException {
    HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
    JsonFactory jsonFactory = new JacksonFactory();
    GoogleCredential credential = GoogleCredential.getApplicationDefault(transport, jsonFactory);

    if (credential.createScopedRequired()) {
      Collection<String> scopes = StorageScopes.all();
      credential = credential.createScoped(scopes);
    }

    return new Storage.Builder(transport, jsonFactory, credential)
        .setApplicationName("AnnotationHive")
        .build();
  }
}
