/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.logging;

import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.transform.AbstractOperationTransformer;
import org.jboss.as.controller.transform.TransformationContext;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
class LoggingOperationTransformer extends AbstractOperationTransformer {
    static final LoggingOperationTransformer INSTANCE = new LoggingOperationTransformer();

    @Override
    protected ModelNode transform(final TransformationContext context, final PathAddress address, final ModelNode operation) {
        final String key = address.getLastElement().getKey();
        final String name = address.getLastElement().getValue();
        final String operationName = operation.get(ModelDescriptionConstants.OP).asString();
        final ModelNode originalOperation = operation.clone();

        // add-handler and remove-handler need to be rewritten
        if (operationName.equals(CommonAttributes.ADD_HANDLER_OPERATION_NAME)) {
            // Determine the resource
            if (key.equals(RootLoggerResourceDefinition.ROOT_LOGGER_PATH_NAME)) {
                operation.get(ModelDescriptionConstants.OP).set(RootLoggerResourceDefinition.ROOT_LOGGER_ADD_HANDLER_OPERATION_NAME);
            } else if (key.equals(LoggerResourceDefinition.LOGGER)) {
                operation.get(ModelDescriptionConstants.OP).set(LoggerResourceDefinition.LEGACY_ADD_HANDLER_OPERATION_NAME);
            } else if (key.equals(AsyncHandlerResourceDefinition.ASYNC_HANDLER)) {
                operation.get(ModelDescriptionConstants.OP).set(AsyncHandlerResourceDefinition.ADD_SUBHANDLER_OPERATION_NAME);
            }
        } else if (operationName.equals(CommonAttributes.REMOVE_HANDLER_OPERATION_NAME)) {
            // Determine the resource
            if (key.equals(RootLoggerResourceDefinition.ROOT_LOGGER_PATH_NAME)) {
                operation.get(ModelDescriptionConstants.OP).set(RootLoggerResourceDefinition.ROOT_LOGGER_REMOVE_HANDLER_OPERATION_NAME);
            } else if (key.equals(LoggerResourceDefinition.LOGGER)) {
                operation.get(ModelDescriptionConstants.OP).set(LoggerResourceDefinition.LEGACY_REMOVE_HANDLER_OPERATION_NAME);
            } else if (key.equals(AsyncHandlerResourceDefinition.ASYNC_HANDLER)) {
                operation.get(ModelDescriptionConstants.OP).set(AsyncHandlerResourceDefinition.REMOVE_SUBHANDLER_OPERATION_NAME);
            }
        } else if (operationName.equals(ModelDescriptionConstants.WRITE_ATTRIBUTE_OPERATION)) {
            // write-attribute needs to fix the formatter and the filter-spec
            // Get the attribute being written to
            final String attributeName = operation.get(ModelDescriptionConstants.NAME).asString();
            // Do not process expressions as they were not supported in previous versions
            if (operation.get(ModelDescriptionConstants.VALUE).getType() != ModelType.EXPRESSION) {
                // Check for the color in the pattern format
                if (attributeName.equals(CommonAttributes.FORMATTER.getName())) {
                    final String currentPattern = operation.get(ModelDescriptionConstants.VALUE).asString();
                    operation.get(ModelDescriptionConstants.VALUE).set(Logging.fixFormatPattern(currentPattern));
                } else if (attributeName.equals(CommonAttributes.FILTER_SPEC.getName())) {
                    // Fix the filter-spec
                    operation.get(ModelDescriptionConstants.NAME).set(CommonAttributes.FILTER.getName());
                    final String filterExpression = operation.get(ModelDescriptionConstants.VALUE).asString();
                    operation.get(ModelDescriptionConstants.VALUE).set(Filters.filterSpecToFilter(filterExpression));
                } else if (attributeName.equals(CommonAttributes.ENABLED.getName())) {
                    final boolean enabled = operation.get(ModelDescriptionConstants.VALUE).asBoolean();
                    if (enabled) {
                        operation.get(ModelDescriptionConstants.OP).set(AbstractHandlerDefinition.ENABLE_HANDLER.getName());
                    } else {
                        operation.get(ModelDescriptionConstants.OP).set(AbstractHandlerDefinition.DISABLE_HANDLER.getName());
                    }
                    operation.remove(ModelDescriptionConstants.NAME);
                    operation.remove(ModelDescriptionConstants.VALUE);
                }
            }
        } else if (operationName.equals(ModelDescriptionConstants.ADD)) {
            // Category or name is required for add operations
            if (LoggerResourceDefinition.LOGGER.equals(key)) {
                operation.get(LoggerResourceDefinition.CATEGORY.getName()).set(name);
            } else if (!RootLoggerResourceDefinition.ROOT_LOGGER_PATH_NAME.equals(key)) {
                // Add the name to handlers
                operation.get(CommonAttributes.NAME.getName()).set(name);
            }
        } else if (operationName.equals(RootLoggerResourceDefinition.ROOT_LOGGER_ADD_OPERATION_NAME)) {
            // set-root-logger operation can't have a name attribute
            operation.remove(CommonAttributes.NAME.getName());
        }
        // Check for the color in the pattern format. Do not process expressions as they were not supported in previous versions
        if (operation.hasDefined(CommonAttributes.FORMATTER.getName()) && operation.get(CommonAttributes.FORMATTER.getName()).getType() != ModelType.EXPRESSION) {
            final String currentPattern = operation.get(CommonAttributes.FORMATTER.getName()).asString();
            operation.get(CommonAttributes.FORMATTER.getName()).set(Logging.fixFormatPattern(currentPattern));
        }
        // Fix the filter. Do not process expressions as they were not supported in previous version
        if (operation.hasDefined(CommonAttributes.FILTER_SPEC.getName()) && operation.get(CommonAttributes.FILTER_SPEC.getName()).getType() != ModelType.EXPRESSION) {
            final String filterExpression = operation.get(CommonAttributes.FILTER_SPEC.getName()).asString();
            operation.get(CommonAttributes.FILTER.getName()).set(Filters.filterSpecToFilter(filterExpression));
        }
        // Always remove the filter-spec
        operation.remove(CommonAttributes.FILTER_SPEC.getName());
        // Always remove the enable attribute
        operation.remove(CommonAttributes.ENABLED.getName());

        LoggingLogger.ROOT_LOGGER.tracef("Changed operation from: %s%nto: %s", originalOperation, operation);
        return operation;
    }
}
