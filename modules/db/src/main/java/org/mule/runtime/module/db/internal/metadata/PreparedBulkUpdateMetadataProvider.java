/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.runtime.module.db.internal.metadata;

import org.mule.common.DefaultResult;
import org.mule.common.Result;
import org.mule.common.metadata.DefaultListMetaDataModel;
import org.mule.common.metadata.DefaultMetaData;
import org.mule.common.metadata.MetaData;
import org.mule.runtime.module.db.internal.domain.autogeneratedkey.AutoGeneratedKeyStrategy;
import org.mule.runtime.module.db.internal.domain.query.Query;
import org.mule.runtime.module.db.internal.resolver.database.DbConfigResolver;

import java.sql.PreparedStatement;

/**
 * Provides metadata for prepared bulk update queries
 */
public class PreparedBulkUpdateMetadataProvider extends UpdateMetadataProvider {


  public PreparedBulkUpdateMetadataProvider(DbConfigResolver dbConfigResolver, Query query,
                                            AutoGeneratedKeyStrategy autoGeneratedKeyStrategy) {
    super(dbConfigResolver, query, autoGeneratedKeyStrategy);
  }

  @Override
  public Result<MetaData> getStaticOutputMetadata() {
    Result<MetaData> dynamicInputMetadata = super.getStaticOutputMetadata();

    if (requiresWrappingMetadata(dynamicInputMetadata)) {
      DefaultListMetaDataModel listModel = new DefaultListMetaDataModel(dynamicInputMetadata.get().getPayload(), true);
      DefaultMetaData defaultMetaData = new DefaultMetaData(listModel);
      return new DefaultResult<MetaData>(defaultMetaData);
    } else {
      return dynamicInputMetadata;
    }
  }

  @Override
  public Result<MetaData> getDynamicOutputMetadata(PreparedStatement statement) {
    return getStaticOutputMetadata();
  }

  @Override
  public Result<MetaData> getDynamicInputMetadata(PreparedStatement statement, Query query) {
    Result<MetaData> dynamicInputMetadata = super.getDynamicInputMetadata(statement, query);

    if (requiresWrappingMetadata(dynamicInputMetadata)) {
      DefaultListMetaDataModel listModel = new DefaultListMetaDataModel(dynamicInputMetadata.get().getPayload());
      DefaultMetaData defaultMetaData = new DefaultMetaData(listModel);
      return new DefaultResult<MetaData>(defaultMetaData);
    } else {
      return dynamicInputMetadata;
    }
  }

  private boolean requiresWrappingMetadata(Result<MetaData> dynamicInputMetadata) {
    return dynamicInputMetadata != null && dynamicInputMetadata.getStatus() == Result.Status.SUCCESS;
  }
}