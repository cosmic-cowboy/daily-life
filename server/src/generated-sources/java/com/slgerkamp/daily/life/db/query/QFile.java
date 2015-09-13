package com.slgerkamp.daily.life.db.query;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QFile is a Querydsl query type for QFile
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QFile extends com.querydsl.sql.RelationalPathBase<QFile> {

    private static final long serialVersionUID = -1516511187;

    public static final QFile file = new QFile("FILE");

    public final DateTimePath<java.sql.Timestamp> createDate = createDateTime("createDate", java.sql.Timestamp.class);

    public final NumberPath<Long> fileId = createNumber("fileId", Long.class);

    public final com.querydsl.sql.PrimaryKey<QFile> constraint2 = createPrimaryKey(fileId);

    public QFile(String variable) {
        super(QFile.class, forVariable(variable), "PUBLIC", "FILE");
        addMetadata();
    }

    public QFile(String variable, String schema, String table) {
        super(QFile.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QFile(Path<? extends QFile> path) {
        super(path.getType(), path.getMetadata(), "PUBLIC", "FILE");
        addMetadata();
    }

    public QFile(PathMetadata metadata) {
        super(QFile.class, metadata, "PUBLIC", "FILE");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createDate, ColumnMetadata.named("CREATE_DATE").withIndex(2).ofType(Types.TIMESTAMP).withSize(23).withDigits(10).notNull());
        addMetadata(fileId, ColumnMetadata.named("FILE_ID").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
    }

}

