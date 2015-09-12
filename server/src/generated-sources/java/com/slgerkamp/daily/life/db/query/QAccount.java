package com.slgerkamp.daily.life.db.query;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QAccount is a Querydsl query type for QAccount
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QAccount extends com.querydsl.sql.RelationalPathBase<QAccount> {

    private static final long serialVersionUID = 53990876;

    public static final QAccount account = new QAccount("ACCOUNT");

    public final DateTimePath<java.sql.Timestamp> createDate = createDateTime("createDate", java.sql.Timestamp.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final com.querydsl.sql.PrimaryKey<QAccount> constraintE4 = createPrimaryKey(email);

    public QAccount(String variable) {
        super(QAccount.class, forVariable(variable), "PUBLIC", "ACCOUNT");
        addMetadata();
    }

    public QAccount(String variable, String schema, String table) {
        super(QAccount.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QAccount(Path<? extends QAccount> path) {
        super(path.getType(), path.getMetadata(), "PUBLIC", "ACCOUNT");
        addMetadata();
    }

    public QAccount(PathMetadata metadata) {
        super(QAccount.class, metadata, "PUBLIC", "ACCOUNT");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createDate, ColumnMetadata.named("CREATE_DATE").withIndex(3).ofType(Types.TIMESTAMP).withSize(23).withDigits(10).notNull());
        addMetadata(email, ColumnMetadata.named("EMAIL").withIndex(2).ofType(Types.VARCHAR).withSize(2147483647).notNull());
        addMetadata(userId, ColumnMetadata.named("USER_ID").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
    }

}

