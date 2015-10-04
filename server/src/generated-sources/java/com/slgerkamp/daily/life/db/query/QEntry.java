package com.slgerkamp.daily.life.db.query;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QEntry is a Querydsl query type for QEntry
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QEntry extends com.querydsl.sql.RelationalPathBase<QEntry> {

    private static final long serialVersionUID = 232027105;

    public static final QEntry entry = new QEntry("ENTRY");

    public final StringPath content = createString("content");

    public final DateTimePath<java.sql.Timestamp> createDate = createDateTime("createDate", java.sql.Timestamp.class);

    public final NumberPath<Long> entryId = createNumber("entryId", Long.class);

    public final NumberPath<Long> fileId = createNumber("fileId", Long.class);

    public final StringPath postDate = createString("postDate");

    public final DateTimePath<java.sql.Timestamp> updateDate = createDateTime("updateDate", java.sql.Timestamp.class);

    public final com.querydsl.sql.PrimaryKey<QEntry> constraint3 = createPrimaryKey(entryId);

    public final com.querydsl.sql.ForeignKey<QFile> fkeyEntryFileId = createForeignKey(fileId, "FILE_ID");

    public QEntry(String variable) {
        super(QEntry.class, forVariable(variable), "PUBLIC", "ENTRY");
        addMetadata();
    }

    public QEntry(String variable, String schema, String table) {
        super(QEntry.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QEntry(Path<? extends QEntry> path) {
        super(path.getType(), path.getMetadata(), "PUBLIC", "ENTRY");
        addMetadata();
    }

    public QEntry(PathMetadata metadata) {
        super(QEntry.class, metadata, "PUBLIC", "ENTRY");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(content, ColumnMetadata.named("CONTENT").withIndex(2).ofType(Types.VARCHAR).withSize(2147483647).notNull());
        addMetadata(createDate, ColumnMetadata.named("CREATE_DATE").withIndex(5).ofType(Types.TIMESTAMP).withSize(23).withDigits(10).notNull());
        addMetadata(entryId, ColumnMetadata.named("ENTRY_ID").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(fileId, ColumnMetadata.named("FILE_ID").withIndex(3).ofType(Types.BIGINT).withSize(19));
        addMetadata(postDate, ColumnMetadata.named("POST_DATE").withIndex(4).ofType(Types.VARCHAR).withSize(2147483647).notNull());
        addMetadata(updateDate, ColumnMetadata.named("UPDATE_DATE").withIndex(6).ofType(Types.TIMESTAMP).withSize(23).withDigits(10).notNull());
    }

}

