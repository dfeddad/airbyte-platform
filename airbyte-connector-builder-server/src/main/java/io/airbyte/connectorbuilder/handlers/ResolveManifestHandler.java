/*
 * Copyright (c) 2020-2025 Airbyte, Inc., all rights reserved.
 */

package io.airbyte.connectorbuilder.handlers;

import io.airbyte.connectorbuilder.TracingHelper;
import io.airbyte.connectorbuilder.api.model.generated.ResolveManifest;
import io.airbyte.connectorbuilder.api.model.generated.ResolveManifestRequestBody;
import io.airbyte.connectorbuilder.exceptions.AirbyteCdkInvalidInputException;
import io.airbyte.connectorbuilder.exceptions.ConnectorBuilderException;
import io.airbyte.connectorbuilder.requester.AirbyteCdkRequester;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handle /manifest/resolve requests.
 */
@Singleton
public class ResolveManifestHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ResolveManifestHandler.class);

  private final AirbyteCdkRequester requester;

  @Inject
  public ResolveManifestHandler(
                                final AirbyteCdkRequester requester) {
    this.requester = requester;
  }

  /**
   * Use the requester to send the resolve_manifest request to the CDK.
   */
  public ResolveManifest resolveManifest(
                                         final ResolveManifestRequestBody resolveManifestRequestBody)
      throws AirbyteCdkInvalidInputException, ConnectorBuilderException {
    try {
      TracingHelper.addWorkspaceAndProjectIdsToTrace(resolveManifestRequestBody.getWorkspaceId(), resolveManifestRequestBody.getProjectId());
      LOGGER.info("Handling resolve_manifest request for workspace '{}' with project ID = '{}'",
          resolveManifestRequestBody.getWorkspaceId(), resolveManifestRequestBody.getProjectId());
      return this.requester.resolveManifest(resolveManifestRequestBody.getManifest());
    } catch (final IOException exc) {
      LOGGER.error("Error handling resolve_manifest request.", exc);
      throw new ConnectorBuilderException("Error handling resolve_manifest request.", exc);
    }
  }

}
