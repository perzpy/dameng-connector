/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.connector.dameng;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.debezium.connector.dameng.antlr.OracleDdlParser;
import io.debezium.pipeline.spi.SchemaChangeEventEmitter;
import io.debezium.relational.Table;
import io.debezium.relational.TableId;
import io.debezium.relational.Tables;
import io.debezium.schema.SchemaChangeEvent;
import io.debezium.schema.SchemaChangeEvent.SchemaChangeEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * {@link SchemaChangeEventEmitter} implementation based on Oracle.
 *
 * @author Gunnar Morling
 */
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2"})
public class BaseOracleSchemaChangeEventEmitter
        implements SchemaChangeEventEmitter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseOracleSchemaChangeEventEmitter.class);

    private final DamengOffsetContext offsetContext;
    private final TableId tableId;
    private final String sourceDatabaseName;
    private final String objectOwner;
    private final String ddlText;
    private final String commandType;

    public BaseOracleSchemaChangeEventEmitter(DamengOffsetContext offsetContext, TableId tableId,
            String sourceDatabaseName, String objectOwner, String ddlText,
            String commandType)
    {
        this.offsetContext = offsetContext;
        this.tableId = tableId;
        this.sourceDatabaseName = sourceDatabaseName;
        this.objectOwner = objectOwner;
        this.ddlText = ddlText;
        this.commandType = commandType;
    }

    @Override
    public void emitSchemaChangeEvent(Receiver receiver)
            throws InterruptedException
    {
        SchemaChangeEventType eventType = getSchemaChangeEventType();
        if (eventType == null) {
            return;
        }

        Tables tables = new Tables();

        OracleDdlParser parser = new OracleDdlParser();
        parser.setCurrentDatabase(sourceDatabaseName);
        parser.setCurrentSchema(objectOwner);
        parser.parse(ddlText, tables);

        Set<TableId> changedTableIds = tables.drainChanges();
        if (changedTableIds.isEmpty()) {
            throw new IllegalArgumentException("Couldn't parse DDL statement " + ddlText);
        }

        Table table = tables.forTable(tableId);

        receiver.schemaChangeEvent(new SchemaChangeEvent(
                offsetContext.getPartition(),
                offsetContext.getOffset(),
                offsetContext.getSourceInfo(),
                sourceDatabaseName,
                objectOwner,
                ddlText,
                table,
                eventType,
                false));
    }

    private SchemaChangeEventType getSchemaChangeEventType()
    {
        switch (commandType) {
            case "CREATE TABLE":
                return SchemaChangeEventType.CREATE;
            case "ALTER TABLE":
                LOGGER.warn("ALTER TABLE not yet implemented");
                break;
            case "DROP TABLE":
                LOGGER.warn("DROP TABLE not yet implemented");
                break;
            default:
                LOGGER.debug("Ignoring DDL event of type {}", commandType);
        }

        return null;
    }
}
